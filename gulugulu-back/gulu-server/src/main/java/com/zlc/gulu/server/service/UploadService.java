package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.vo.ChunkVo;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Result uploadChunk(ChunkVo chunkVo);

    boolean mergeChunk(String hash, int total);

    Result uploadCover(MultipartFile cover);
}
