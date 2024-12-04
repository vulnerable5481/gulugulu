package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.common.constant.CommentConstant;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.pojo.entity.CommentEntity;
import com.zlc.gulu.pojo.entity.UserEntity;
import com.zlc.gulu.pojo.vo.CommentSendVo;
import com.zlc.gulu.pojo.vo.CommentVo;
import com.zlc.gulu.server.mapper.CommentMapper;
import com.zlc.gulu.server.service.CommentService;
import com.zlc.gulu.server.service.UserService;
import com.zlc.gulu.server.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author 赵联城
 * @description 针对表【gl_comment(评论表)】的数据库操作Service实现
 * @createDate 2024-11-19 18:32:00
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 点赞评论
     *
     * @param
     * @returns
     */
    public Result likeComment(Integer commentId) {
        return Result.success();
    }


    /**
     * 获取评论列表
     *
     * @param offset 分页偏移量 (已经获取的根评论数)
     * @param type   0:热度排行；1：最新排行
     */
    public Result getCommentList(Integer videoId, int offset, int type) {

        // 获取根评论
        List<CommentEntity> roots = getRoots(videoId, offset, type);

        // 判断根评论是否全部被加载
        if (roots.isEmpty()) {
            return Result.error(CommentConstant.CommentNum.COMMENT_LOAD_FINISHED.getCode(), // 返回501错误码,取消滚动加载
                    CommentConstant.CommentNum.COMMENT_LOAD_FINISHED.getMsg());
        }

        // 组装只有单节点的评论树
        List<CommentVo> res = roots.stream().map(root -> {
            return buildCommentTree(root, 0, -1); // todo:后续再优化吧，目前是简单处理，直接获取所有子评论
        }).collect(Collectors.toList());

        // 返回数据
        return Result.success(res);
    }

    /**
     * 根据分页偏移量获取根评论,每次获取至多十条根评论
     *
     * @param offset 分页偏移量
     * @param type   0:按照热度排序，1：按照最新时间排序
     * @return
     */
    private List<CommentEntity> getRoots(Integer videoId, int offset, int type) {
        String key = type == 0 ? CommentConstant.COMMENT_VIDEO_HOT + videoId :
                CommentConstant.COMMENT_VIDEO_NEWEST + videoId;
        List<CommentEntity> roots = null;

        // 查询redis缓存
        Set<String> set = redisUtils.zReverange(key, offset, offset + 9);
        if (set != null && set.size() > 0) {
            List<Long> ids = set.stream().map(item ->
                    Long.parseLong(item)
            ).collect(Collectors.toList());
            // 刷新缓存
            redisUtils.expire(key, CommentConstant.COMMENT_VIDEO_EXPIRE, CommentConstant.COMMENT_VIDEO_TIMEUNIT);
            // 根据根评论Ids查询出所需根评论
            roots = type == 0 ?
                    this.getBaseMapper().selectList(new LambdaQueryWrapper<CommentEntity>().in(CommentEntity::getCommentId, ids)
                            .orderByDesc(CommentEntity::getLikeCount)) :
                    this.getBaseMapper().selectList(new LambdaQueryWrapper<CommentEntity>().in(CommentEntity::getCommentId, ids)
                            .orderByDesc(CommentEntity::getCreateTime));
            // 返回数据
            return roots;
        }

        // 缓存未命中,查询数据库
        roots = type == 0 ? this.getBaseMapper().listTenRootsOrderByLikeCount(videoId, 0, 0,offset, 10)
                : this.getBaseMapper().listTenRootsOrderByTime(videoId, 0, 0, offset, 10);
        // 判断视频是否还存在根评论
        if (roots == null && roots.size() == 0) {
            return Collections.emptyList();
        }

        // 异步更新所有根评论->reids缓存
        CompletableFuture.runAsync(() -> {
            // 按照时间更新
            String newestKey = CommentConstant.COMMENT_VIDEO_NEWEST + videoId; // key
            List<CommentEntity> newestRoots =           // value
                    this.list(
                            new LambdaQueryWrapper<CommentEntity>().eq(CommentEntity::getVideoId, videoId)
                                    .eq(CommentEntity::getRootId, 0).eq(CommentEntity::getStatus, 0));
            List<RedisUtils.ZObjTime> zObjtimeCollections = newestRoots.stream().parallel().map(root -> {
                Integer id = root.getCommentId();
                // 指定日期时间格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                // 将字符串转换为 LocalDateTime
                LocalDateTime localDateTime = LocalDateTime.parse(root.getCreateTime(), formatter);
                // 转换为时间戳（秒级）
                long timestamp = localDateTime.toEpochSecond(ZoneOffset.UTC); // 使用 UTC 时区
                return new RedisUtils.ZObjTime(id, timestamp);
            }).collect(Collectors.toList());
            redisUtils.zsetBatch(newestKey, zObjtimeCollections);
            redisUtils.expire(newestKey, CommentConstant.COMMENT_VIDEO_EXPIRE, CommentConstant.COMMENT_VIDEO_TIMEUNIT);

            // 按照热度更新
            String hotHkey = CommentConstant.COMMENT_VIDEO_HOT + videoId;   // key
            List<CommentEntity> hotRoots = this.list(            // value
                    new LambdaQueryWrapper<CommentEntity>().eq(CommentEntity::getVideoId, videoId)
                            .eq(CommentEntity::getRootId, 0).eq(CommentEntity::getStatus, 0)
            );
            List<RedisUtils.ZObjScore> zObjScoreCollections = hotRoots.stream().parallel().map(root ->
                    new RedisUtils.ZObjScore(root.getCommentId(), root.getLikeCount().doubleValue())
            ).collect(Collectors.toList());
            redisUtils.zsetBatchByScore(hotHkey, zObjScoreCollections);
            redisUtils.expire(hotHkey, CommentConstant.COMMENT_VIDEO_EXPIRE, CommentConstant.COMMENT_VIDEO_TIMEUNIT);
        });

        return roots;
    }

    /*
     * 生成评论树
     * */
    private CommentVo buildCommentTree(CommentEntity root, long start, long end) {
        // 构建commentVo
        CommentVo commentVo = new CommentVo();
        commentVo = converToCommentVo(root);

        String key = CommentConstant.COMMENT_REPLY + root.getVideoId() + ":" + root.getCommentId();
        Set<String> set = redisUtils.zRange(key, start, end);
        List<CommentEntity> children = null;
        // 查询redis缓存
        if (set != null && set.size() > 0) {
            // 缓存命中
            List<Long> ids = set.stream().map(id -> Long.parseLong(id)).collect(Collectors.toList());
            children = this.getBaseMapper().selectBatchIds(ids); // 查询【start,end】范围内的所有子评论
        }
        if (children == null || children.size() == 0) {
            // 缓存未命中
            // todo:有待优化，缓存未命中不就要全部查询了吗，那不就G了？
            children = this.list(new LambdaQueryWrapper<CommentEntity>().eq(CommentEntity::getVideoId,
                    root.getVideoId()).eq(CommentEntity::getRootId, root.getCommentId()).eq(CommentEntity::getStatus,
                    0));
            // 判断根评论是否存在子评论
            if (children == null || children.size() == 0) {
                return commentVo;
            }
            // 异步更新到缓存中
            List<CommentEntity> finalChildren = children;
            CompletableFuture.runAsync(() -> {
                List<RedisUtils.ZObjTime> collections = finalChildren.stream().map(child -> {
                    Integer id = child.getCommentId();
                    long l = System.currentTimeMillis();
                    return new RedisUtils.ZObjTime(id, l);
                }).collect(Collectors.toList());
                redisUtils.zsetBatch(key, collections); // 批量添加到缓存
                redisUtils.expire(key, CommentConstant.COMMENT_VIDEO_EXPIRE, CommentConstant.COMMENT_VIDEO_TIMEUNIT);
            });
        }

        // 子评论转换
        List<CommentVo> list = children.stream().map(child -> converToCommentVo(child)).collect(Collectors.toList());
        commentVo.setChildren(list);

        // 返回数据
        return commentVo;
    }

    /*           转换
     *  comment ----->  commentVo (填充信息)
     * */
    private CommentVo converToCommentVo(CommentEntity root) {
        // 构建commentVo
        CommentVo commentVo = new CommentVo();

        // 获取用户信息
        // todo:同一个评论区很有可能出现单一用户多次评论，所以可以使用HashSet/redis缓存,存储用户信息，短时间内只需要查询一次数据库即可完成信息填充
        BeanUtils.copyProperties(root, commentVo);
        UserEntity user = userService.queryById(commentVo.getUserId());
        commentVo.setUserName(user == null ? "已注销" : user.getUserName());
        commentVo.setUserAvatar(user == null ? "" : user.getAvatar());
        // 如果不是根评论，则需要查找回复用户
        if (commentVo.getRootId() != 0) {
            UserEntity replyUser = userService.queryById(commentVo.getReplyId());
            commentVo.setReplyName(replyUser == null ? "已注销" : replyUser.getUserName());
        }
        return commentVo;
    }

    /*
     * 查询十条评论
     * */

    /*
     *  发送评论
     * */
    @Override
    @Transactional
    public Result sendComment(CommentSendVo commentSendVo) {
        CommentEntity commentEntity = new CommentEntity();
        BeanUtils.copyProperties(commentSendVo, commentEntity);
        commentEntity.setCreateTime(GuluUtils.now()); // 这里需要显示声明创建时间，是为了及时给前端回馈数据

        // 插入数据库
        this.save(commentEntity);

        // 异步操作
        CompletableFuture.runAsync(() -> {
            String value = commentEntity.getCommentId().toString();
            // 存储到redis缓存
            if (commentEntity.getRootId() != 0) {
                // 不是根评论
                String key =
                        CommentConstant.COMMENT_REPLY + commentEntity.getVideoId() + ":" + commentEntity.getRootId();
                redisUtils.zset(key, value);
                redisUtils.expire(key, CommentConstant.COMMENT_VIDEO_EXPIRE, CommentConstant.COMMENT_VIDEO_TIMEUNIT);
            } else {
                // 是根评论
                // newest
                String newestKey = CommentConstant.COMMENT_VIDEO_NEWEST + commentEntity.getVideoId();
                redisUtils.zset(newestKey, value);
                redisUtils.expire(newestKey, CommentConstant.COMMENT_VIDEO_EXPIRE,
                        CommentConstant.COMMENT_VIDEO_TIMEUNIT);
                // hot
                String hotKey = CommentConstant.COMMENT_VIDEO_NEWEST + commentEntity.getVideoId();
                redisUtils.zset(hotKey, value);
                redisUtils.expire(hotKey, CommentConstant.COMMENT_VIDEO_EXPIRE, CommentConstant.COMMENT_VIDEO_TIMEUNIT);
            }

            // todo:增加视频评论数
        });

        // 评论回显
        return Result.success(converToCommentVo(commentEntity));
    }

}

































