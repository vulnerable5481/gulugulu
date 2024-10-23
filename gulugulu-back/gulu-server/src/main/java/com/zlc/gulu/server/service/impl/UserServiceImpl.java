package com.zlc.gulu.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.common.constant.RedisConstant;
import com.zlc.gulu.common.constant.UserConstant;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.common.utils.JwtUtils;
import com.zlc.gulu.common.utils.UserHolder;
import com.zlc.gulu.pojo.entity.UserEntity;
import com.zlc.gulu.pojo.vo.UserLoginVo;
import com.zlc.gulu.pojo.vo.UserRegisterVo;
import com.zlc.gulu.pojo.vo.UserVo;
import com.zlc.gulu.server.mapper.UserMapper;
import com.zlc.gulu.server.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zlc.gulu.common.constant.RedisConstant.LOGIN_USER_TTL;

/**
 * @author 赵联城
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-10-11 19:13:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result register(UserRegisterVo userRegisterVo) {

        //1.检验信息 ,部分信息直接前端检验，个人项目不追求严谨!  咕噜~~
        //1.1) 检验name是否为Null
        if (GuluUtils.isEmpty(userRegisterVo.getUserName())) {
            return Result.error(UserConstant.UserLoginEnum.USER_REGISTER_NAME_IS_NULL.getCode()
                    , UserConstant.UserLoginEnum.USER_REGISTER_NAME_IS_NULL.getMsg());
        }

        //1.2) 判断是否已注册
        UserEntity user = this.getOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUserName, userRegisterVo.getUserName())
        );
        if (user != null) {
            return Result.error(UserConstant.UserLoginEnum.USER_ALREADY_EXIST.getCode()
                    , UserConstant.UserLoginEnum.USER_ALREADY_EXIST.getMsg());
        }
        //1.3) 检验pwd是否为null
        if (GuluUtils.isEmpty(userRegisterVo.getPassword())) {
            return Result.error(UserConstant.UserLoginEnum.USER_REGISTER_SECRET_IS_NULL.getCode()
                    , UserConstant.UserLoginEnum.USER_REGISTER_SECRET_IS_NULL.getMsg());
        }
        //1.4) 检验两次密码是否不一致
        if (GuluUtils.isEmpty(userRegisterVo.getMksuerPwd())) {
            return Result.error(UserConstant.UserLoginEnum.USER_REGISTER_SECRET_NOT_SAME.getCode()
                    , UserConstant.UserLoginEnum.USER_REGISTER_SECRET_NOT_SAME.getMsg());
        }

        //2.注册用户
        //2.1) 初始化用户
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRegisterVo, userEntity);
        //密码加盐
        String result = GuluUtils.hashPassword(userRegisterVo.getPassword());
        String[] s = result.split(":");
        userEntity.setSalt(s[0]);
        userEntity.setPassword(s[1]);
        userEntity.setAvatar(UserConstant.USER_DEFAULT_AVATAR);
        userEntity.setStatus(0);
        userEntity.setRoleLevel(0);
        this.save(userEntity);

        //3.返回vo
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userEntity, userVo);
        return Result.success(userVo);
    }

    @Override
    public Result login(UserLoginVo userLoginVo) {
        String name = userLoginVo.getUserName();
        String password = userLoginVo.getPassword();
        UserVo userVo = new UserVo();

        // 1.检验信息
        if (GuluUtils.isEmpty(name)) {
            return Result.error(UserConstant.UserLoginEnum.USER_LOGIN_NAME_WRONG.getCode(),
                    UserConstant.UserLoginEnum.USER_LOGIN_NAME_WRONG.getMsg());
        }
        if (GuluUtils.isEmpty(password)) {
            return Result.error(UserConstant.UserLoginEnum.USER_LOGIN_SECRET_WRONG.getCode(),
                    UserConstant.UserLoginEnum.USER_LOGIN_SECRET_WRONG.getMsg());
        }

        // 2.判断用户是否存在
        //2.1)查询缓存
        UserVo user = UserHolder.getUser();
        if (!GuluUtils.isEmpty(user) && !GuluUtils.isEmpty(user.getUserId())) {
            return Result.success(user);
        }
        //2.2)查询数据库
        UserEntity userEntity = this.getOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUserName, name)
        );
        if (GuluUtils.isEmpty(userEntity.getUserId()) && GuluUtils.isEmpty(userEntity)) {
            return Result.error(UserConstant.UserLoginEnum.USER_LOGIN_USER_IS_NULL.getCode(),
                    UserConstant.UserLoginEnum.USER_LOGIN_USER_IS_NULL.getMsg());
        }
        String salt = userEntity.getSalt();
        String pwd = userEntity.getPassword();
        if (!GuluUtils.verifyPassword(password, salt, pwd)) {
            return Result.error(UserConstant.UserLoginEnum.USER_LOGIN_SECRET_WRONG.getCode(),
                    UserConstant.UserLoginEnum.USER_LOGIN_SECRET_WRONG.getMsg());
        }

        //3.保存用户信息到 redis中
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", userEntity.getUserId().toString());
        String token = JwtUtils.createJWT(claims);
        //todo:研究反射之后，自己亲自完成这个功能，本项目所有用到的方法全部自己来封装！
        BeanUtils.copyProperties(userEntity, userVo);
        Map<String, Object> userMap = BeanUtil.beanToMap(userVo, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue != null ? fieldValue.toString() : null));
        String tokenKey = RedisConstant.LOGIN_TOKEN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

        userVo.setToken(token);
        return Result.success(userVo);
    }
}




