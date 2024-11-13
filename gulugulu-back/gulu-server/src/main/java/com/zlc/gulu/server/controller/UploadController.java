package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.vo.ChunkVo;
import com.zlc.gulu.server.service.UploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    public UploadService uploadService;

    /*
     * 视频分片上传
     * */
    @PostMapping("/chunk")
    public Result uploadVideo(@ModelAttribute("formData") ChunkVo chunkVo) {
        return uploadService.uploadChunk(chunkVo);
    }

    /*
     *  图片上传
     * */
    @PostMapping("/img")
    public Result uploadImg(@RequestParam("cover") MultipartFile cover) {
        return this.uploadService.uploadCover(cover);
    }
}
