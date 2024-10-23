package com.zlc.gulu.server.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.zlc.gulu.common.constant.RedisConstant;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.common.utils.UserHolder;
import com.zlc.gulu.pojo.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //取token
        String token = request.getHeader("authorization");
        if (GuluUtils.isEmpty(token)) {
            return true;
        }
        //根据token查询用户
        String key = RedisConstant.LOGIN_TOKEN_USER_KEY + token;
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        //用户不存在
        if(map.isEmpty()){
            return true;
        }

        UserVo userVo = BeanUtil.fillBeanWithMap(map, new UserVo(), false);
        //保存用户
        UserHolder.saveUser(userVo);
        //刷新token
        stringRedisTemplate.expire(key, RedisConstant.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return true;
    }
}
