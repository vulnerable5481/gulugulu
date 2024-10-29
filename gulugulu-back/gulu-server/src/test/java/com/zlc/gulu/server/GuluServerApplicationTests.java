package com.zlc.gulu.server;

import cn.hutool.core.bean.BeanUtil;
import com.zlc.gulu.pojo.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
class GuluServerApplicationTests {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testRedis(){
        String tokenKey = "login:token:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzAxMjAyNTMsInVzZXJJZCI6IjMifQ.ne4bbEmujPS_r2jvnWsDiHzXqO6cFLLJUKwvtQYAN_o";
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(tokenKey);
        System.out.println(entries);
        UserEntity userEntity = BeanUtil.toBean(entries, UserEntity.class);
        System.out.println(userEntity);
    }

    @Test
    void testDeleteFile(){

    }

}
