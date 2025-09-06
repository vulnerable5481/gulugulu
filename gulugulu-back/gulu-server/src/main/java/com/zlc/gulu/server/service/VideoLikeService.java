package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.VideoLikeEntity;

/**
* @author 赵联城
* @description 针对表【gl_video_like(关联用户点赞表)】的数据库操作Service
* @createDate 2025-03-03 13:42:01
*/
public interface VideoLikeService extends IService<VideoLikeEntity> {

    boolean isLike(Integer userId,Integer videoId);

    Result like(Integer userId, Integer videoId);
}
