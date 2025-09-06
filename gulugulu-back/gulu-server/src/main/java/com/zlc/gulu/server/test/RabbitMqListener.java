package com.zlc.gulu.server.test;

import com.rabbitmq.client.Channel;
import com.zlc.gulu.pojo.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqListener {

    @RabbitListener(queues = "gulu_order_queue1")
    public void listenOrderQueue(UserEntity user, Channel channel, Message message) {
        log.info("监听到gulu_order_queue1的消息：" + user.toString());
        System.out.println("1");
    }
}
