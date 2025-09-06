package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.dto.CountOnlineUserDTO;
import com.zlc.gulu.pojo.entity.VideoEntity;
import com.zlc.gulu.pojo.vo.VideoUploadVo;
import com.zlc.gulu.server.service.CommentService;
import com.zlc.gulu.server.service.VideoLikeService;
import com.zlc.gulu.server.service.VideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private VideoService videoService;

    @Resource
    private VideoLikeService videoLikeService;

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
    public Result queryVideo(@RequestParam("videoId") Integer videoId) {
        return videoService.queryVideoById(videoId);
    }


    /*
    *  判断用户是否点赞视频
    * */
    @PostMapping("/islike")
    public Result isLike(@RequestParam("userId") Integer userId,@RequestParam("videoId") Integer videoId){
        boolean islike = videoLikeService.isLike(userId, videoId);
        return Result.success(islike);
    }

    /*
    *  点赞或取消点赞视频
    * */
    @PostMapping("/like")
    public Result like(@RequestParam("userId") Integer userId,@RequestParam("videoId") Integer videoId){
        return videoLikeService.like(userId,videoId);
    }

    /*
    *  统计实时在线观看人数
    * */
    @PostMapping("/countOnline")
    public Result countOnline(int vid){
        return videoService.countOnline(vid);
    }

}
























