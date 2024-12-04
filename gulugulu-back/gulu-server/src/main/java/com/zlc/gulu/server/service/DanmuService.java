package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.DanmuEntity;

/**
* @author 赵联城
* @description 针对表【gl_danmu(弹幕表)】的数据库操作Service
* @createDate 2024-12-01 18:35:25
*/
public interface DanmuService extends IService<DanmuEntity> {

    Result sendDanmu(DanmuEntity danmu);

    Result listDanmu(Integer videoId);
}
