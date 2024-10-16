package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.pojo.entity.UserEntity;
import com.zlc.gulu.server.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 赵联城
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-10-11 19:13:09
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
    implements IService<UserEntity> {

}




