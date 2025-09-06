package com.zlc.gulu.server;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zlc.gulu.pojo.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class GuluServerApplicationTests {
    @Resource
    RabbitTemplate rabbitTemplate;


    @Test
    void contextLoads() {
    }

    @Test
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("gulu_order_queue1"),
            exchange = @Exchange("gulu_order_exchange1"),
            key = {"member"}
    ))
    void testMQ() throws IOException, TimeoutException {
        String exchange = "gulu_order_exchange1";
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("赵联城");
        userEntity.setPassword("123");
        String routeKey = "member";
        rabbitTemplate.convertAndSend(exchange, routeKey, userEntity);
        System.out.println("消息发送成功!");

    }


}
