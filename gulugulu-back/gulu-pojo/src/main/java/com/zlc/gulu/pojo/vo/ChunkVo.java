package com.zlc.gulu.pojo.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChunkVo {
    //分片索引
    private Integer id;
    //分片内容
    private MultipartFile data;
    //hash
    private String hash;
    //单一分片大小
    private Long size;
    //总分片数
    private Integer total;
}
