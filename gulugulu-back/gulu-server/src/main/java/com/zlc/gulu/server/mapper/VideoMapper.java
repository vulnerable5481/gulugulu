package com.zlc.gulu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.gulu.pojo.entity.VideoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 赵联城
 * @description 针对表【video】的数据库操作Mapper
 * @createDate 2024-11-10 16:22:07
 * @Entity com.zlc.gulu.pojo.entity.VideoEntity
 */

@Mapper
public interface VideoMapper extends BaseMapper<VideoEntity> {

}
