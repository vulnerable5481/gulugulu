package com.zlc.gulu.server.config;

import com.zlc.gulu.server.interceptor.RefreshTokenInterceptor;
import com.zlc.gulu.server.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 刷新token
        RefreshTokenInterceptor refreshTokenInterceptor = new RefreshTokenInterceptor(stringRedisTemplate);
        registry.addInterceptor(refreshTokenInterceptor)
                .addPathPatterns("/**").order(0);
        // 判断用户是否有权限访问
        JwtInterceptor jwtInterceptor = new JwtInterceptor();
        registry.addInterceptor(jwtInterceptor)
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/video/randomViews"  // 未登录也允许获取视频
                ).order(1);
    }



}
