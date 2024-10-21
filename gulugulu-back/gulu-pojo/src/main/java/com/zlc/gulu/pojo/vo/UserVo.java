package com.zlc.gulu.pojo.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer userId;
    private String UserName;
    private String avatar;
    private Integer roleLevel;
    private Integer status;
    private String token;
}
