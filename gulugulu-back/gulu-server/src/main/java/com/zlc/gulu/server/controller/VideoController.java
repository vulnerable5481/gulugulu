package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.VideoEntity;
import com.zlc.gulu.pojo.vo.VideoUploadVo;
import com.zlc.gulu.server.service.VideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private VideoService videoService;

    /*
     *  返回随机视频
     *  TODO:此处返回给前端11个随机视频，关于feed流推送，以后再说吧~~~
     * */
    @GetMapping("/randomViews")
    public Result getRandomViews() {
        List<VideoEntity> list = videoService.getRandomViews();
        return Result.success(list);
    }

    /*
     *  上传投稿视频
     * */
    @PostMapping("/save")
    public Result saveVideo(@RequestBody VideoUploadVo videoUploadVo) {
        videoService.saveVideo(videoUploadVo);
        return Result.success();
    }

    /*
     *  获取单个视频详情
     * */
    @GetMapping("/queryVideo")
    public Result queryVideo(@RequestParam("videoId") Integer videoId){
        return videoService.queryVideoById(videoId);
    }
}
