package com.zlc.gulu.pojo.dto;

import lombok.Data;

@Data
public class CountOnlineUserDTO {

    public int vid;

    public int userId;

    // 1就是添加数据 2是移除数据
    public int type;
}
