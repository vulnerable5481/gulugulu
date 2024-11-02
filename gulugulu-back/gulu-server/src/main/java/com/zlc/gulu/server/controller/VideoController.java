package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.VideoEntity;
import com.zlc.gulu.server.service.VideoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private VideoService videoService;

    @GetMapping("/randomViews")
    public Result getRandomViews(){
        List<VideoEntity> list = videoService.getRandomViews();
        return Result.success(list);
    }
}
