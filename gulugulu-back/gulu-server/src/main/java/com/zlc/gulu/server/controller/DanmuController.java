package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.DanmuEntity;
import com.zlc.gulu.server.service.DanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/danmu")
@Slf4j
public class DanmuController {

    @Resource
    private DanmuService danmuService;

    /*
    *  发送弹幕
    * */
    @PostMapping("/send")
    public Result sendDanmu(@RequestBody DanmuEntity danmu){
        return danmuService.sendDanmu(danmu);
    }

    /*
    *  获取弹幕列表
    * */
    @GetMapping("/list")
    public Result listDanmu(@RequestParam("videoId") Integer videoId){
        return danmuService.listDanmu(videoId);
    }
}
