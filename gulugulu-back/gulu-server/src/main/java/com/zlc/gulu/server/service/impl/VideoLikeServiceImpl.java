package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.VideoEntity;
import com.zlc.gulu.pojo.entity.VideoLikeEntity;
import com.zlc.gulu.server.mapper.VideoLikeMapper;
import com.zlc.gulu.server.service.VideoLikeService;
import com.zlc.gulu.server.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * @author 赵联城
 * @description 针对表【gl_video_like(关联用户点赞表)】的数据库操作Service实现
 * @createDate 2025-03-03 13:42:01
 */
@Service
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLikeEntity> implements VideoLikeService {

    @Resource
    private RedisUtils redisUtils;

    /*
     *   判断用户是否点赞视频
     * */
    @Override
    public boolean isLike(Integer userId, Integer videoId) {
        // 查询数据库
        Integer count = this.getBaseMapper().selectCount(
                new LambdaQueryWrapper<VideoLikeEntity>()
                        .eq(VideoLikeEntity::getUserId, userId)
                        .eq(VideoLikeEntity::getVideoId, videoId)
        );
        // todo:查询视频总点赞量 和 点赞关联表 加载到redis缓存
        return count > 0;
    }

    /*
     * 点赞/取消点赞视频
     * */
    @Override
    public Result like(Integer userId, Integer videoId) {
        // todo: 无论点赞还是取消点赞都是修改redis缓存中的数据

        // todo: 最后都要通过定时任务异步地加载到数据库
        CompletableFuture.runAsync(() -> {
            VideoLikeEntity videoLikeEntity = new VideoLikeEntity();
            videoLikeEntity.setUserId(userId);
            videoLikeEntity.setVideoId(videoId);
            this.save(videoLikeEntity);
            // todo: 修改视频点赞量 ,  视频投稿成功还应该初始化添加一个video_detail才对
        });

        return Result.success();
    }
}




