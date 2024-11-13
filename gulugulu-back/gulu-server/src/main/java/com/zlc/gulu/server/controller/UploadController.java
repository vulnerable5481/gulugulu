package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.vo.ChunkVo;
import com.zlc.gulu.pojo.vo.MergeVo;
import com.zlc.gulu.server.service.UploadService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    public UploadService uploadService;

    /*
     * 文件分片上传
     * */
    @PostMapping("/chunk")
    public Result upload(@ModelAttribute("formData") ChunkVo chunkVo) {
        return uploadService.uploadChunk(chunkVo);
    }
}
