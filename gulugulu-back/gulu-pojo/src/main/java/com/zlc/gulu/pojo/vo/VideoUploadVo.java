package com.zlc.gulu.pojo.vo;

import lombok.Data;

@Data
public class VideoUploadVo {
    String title;
    Integer type;
    Integer duration;
    String videoUrl;
    String coverUrl;
    String tags;
    String description;
}
