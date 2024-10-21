package com.zlc.gulu.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * 打通前后端，解决前后端跨域问题
 * */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /*
     * 允许服务器接收任意收到的"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"请求
     * 不限制请求头格式
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
