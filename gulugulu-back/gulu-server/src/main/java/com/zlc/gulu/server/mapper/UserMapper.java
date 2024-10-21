package com.zlc.gulu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.gulu.pojo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 赵联城
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-10-11 19:13:09
* @Entity com.zlc.gulu.pojo.entity.UserEntity
*/
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}




