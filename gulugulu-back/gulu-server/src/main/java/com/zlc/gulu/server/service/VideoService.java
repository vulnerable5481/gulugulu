package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.VideoEntity;
import com.zlc.gulu.pojo.vo.VideoUploadVo;

import java.util.List;

/**
* @author 赵联城
* @description 针对表【video】的数据库操作Service
* @createDate 2024-11-01 12:18:36
*/
public interface VideoService extends IService<VideoEntity> {

    List<VideoEntity> getRandomViews();

    void saveVideo(VideoUploadVo videoUploadVo);

    Result queryVideoById(Integer videoId);
}
