package com.zlc.gulu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.gulu.pojo.entity.DanmuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 赵联城
* @description 针对表【gl_danmu(弹幕表)】的数据库操作Mapper
* @createDate 2024-12-01 18:35:25
* @Entity com.zlc.gulu.generate.DanmuEntity
*/
@Mapper
public interface DanmuMapper extends BaseMapper<DanmuEntity> {

}




