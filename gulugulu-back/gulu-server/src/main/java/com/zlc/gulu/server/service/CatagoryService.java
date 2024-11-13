package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.gulu.pojo.entity.CatagoryEntity;

import java.util.List;

/**
* @author 赵联城
* @description 针对表【gl_catagory(分类表)】的数据库操作Service
* @createDate 2024-11-10 22:07:31
*/
public interface CatagoryService extends IService<CatagoryEntity> {

    List<CatagoryEntity> listWithTree();

}
