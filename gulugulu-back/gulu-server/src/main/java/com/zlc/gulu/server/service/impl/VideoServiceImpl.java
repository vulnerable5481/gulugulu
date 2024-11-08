package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.pojo.entity.VideoEntity;
import com.zlc.gulu.server.mapper.VideoMapper;
import com.zlc.gulu.server.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵联城
 * @description 针对表【video】的数据库操作Service实现
 * @createDate 2024-11-01 12:18:36
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoEntity> implements VideoService {

    @Resource
    private VideoMapper videoMapper;

    /*
     *  判断是否存在 hash匹配的视频
     * */
    @Override
    public boolean isVideoExists(String hash) {
        VideoEntity videoEntity = this.getOne(
                new LambdaQueryWrapper<VideoEntity>().eq(VideoEntity::getHashValue, hash)
        );
        return !GuluUtils.isEmpty(videoEntity);
    }

    /*
     *  返回随机视频
     *  TODO:此处返回给前端11个随机视频，关于feed流推送，以后再说吧~~~
     * */
    @Override
    public List<VideoEntity> getRandomViews() {
        List<VideoEntity> res = videoMapper.selectList(
                new LambdaQueryWrapper<VideoEntity>()
                        .between(VideoEntity::getVideoId, 1, 11)
        );
        return res;
    }
}




