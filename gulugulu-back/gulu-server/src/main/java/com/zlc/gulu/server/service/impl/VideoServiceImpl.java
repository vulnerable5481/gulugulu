package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.common.constant.VideoConstant;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.common.utils.UserHolder;
import com.zlc.gulu.pojo.entity.CommentEntity;
import com.zlc.gulu.pojo.entity.VideoEntity;
import com.zlc.gulu.pojo.vo.VideoUploadVo;
import com.zlc.gulu.server.mapper.VideoMapper;
import com.zlc.gulu.server.service.CommentService;
import com.zlc.gulu.server.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 赵联城
 * @description 针对表【video】的数据库操作Service实现
 * @createDate 2024-11-01 12:18:36
 */
@Slf4j
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoEntity> implements VideoService {

    @Resource
    private VideoMapper videoMapper;
    @Resource
    private CommentService commentService;

    @Override
    public List<VideoEntity> getRandomViews() {
        List<VideoEntity> res =
                videoMapper.selectList(new LambdaQueryWrapper<VideoEntity>().between(VideoEntity::getVideoId, 103,
                        113));
        return res;
    }

    @Override
    public void saveVideo(VideoUploadVo videoUploadVo) {
        if (videoUploadVo == null) {
            return;
        }
        VideoEntity videoEntity = new VideoEntity();
        BeanUtils.copyProperties(videoUploadVo, videoEntity);
        videoEntity.setUserId(UserHolder.getUser().getUserId());
        videoEntity.setStatus(2);
        // TODO:等以后设置了分区再说吧
        videoEntity.setMpId(1);
        videoEntity.setSpId(1);
        this.save(videoEntity);
    }

    @Override
    public Result queryVideoById(Integer videoId) {
        VideoEntity video = this.getOne(new LambdaQueryWrapper<VideoEntity>().eq(VideoEntity::getVideoId, videoId));
        // 判断视频是否存在/违规
        if (video == null) {
            return Result.error(VideoConstant.VideoEnum.VIDEO_QUERY_NULL.getCode(),
                    VideoConstant.VideoEnum.VIDEO_QUERY_NULL.getMsg());
        }
        if (video.getStatus() == VideoConstant.VIDEO_STATUS_FAIL) {
            return Result.error(VideoConstant.VideoEnum.VIDEO_QUERY_DISABLED.getCode(),
                    VideoConstant.VideoEnum.VIDEO_QUERY_DISABLED.getMsg());
        }
        return Result.success(video);
    }


}




