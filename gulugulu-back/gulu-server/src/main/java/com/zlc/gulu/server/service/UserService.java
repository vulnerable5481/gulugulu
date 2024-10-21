package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.UserEntity;
import com.zlc.gulu.pojo.vo.UserLoginVo;
import com.zlc.gulu.pojo.vo.UserRegisterVo;

/**
* @author 赵联城
* @description 针对表【user】的数据库操作Service
* @createDate 2024-10-11 19:13:09
*/
public interface UserService extends IService<UserEntity> {

    Result register(UserRegisterVo userRegisterVo);

    Result login(UserLoginVo userLoginVo);
}
