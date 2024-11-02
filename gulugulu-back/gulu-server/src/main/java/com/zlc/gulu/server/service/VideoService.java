package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.gulu.pojo.entity.VideoEntity;

import java.util.List;

/**
* @author 赵联城
* @description 针对表【video】的数据库操作Service
* @createDate 2024-11-01 12:18:36
*/
public interface VideoService extends IService<VideoEntity> {

    boolean isVideoExists(String hash);

    List<VideoEntity> getRandomViews();
}
