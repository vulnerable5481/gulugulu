package com.zlc.gulu.server.config;


import com.zlc.gulu.common.enums.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    // 会员订单消费Direct交换机
    @Bean
    public DirectExchange memberDirExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_ORDER_DEAD_CANCEL.exchange).build();
    }

    // 会员订单死信队列
    @Bean
    public Queue memberDeadQueue() {
        return QueueBuilder
                .durable(QueueEnum.QUEUE_ORDER_DEAD_CANCEL.name)
                .build();
    }

    // 会员订单延迟消费队列
    @Bean
    public Queue memberTTLQueue() {
        return QueueBuilder
                // 持久化队列  【保证RabbitMq重启，队列的数据不会丢失！！】
                .durable(QueueEnum.QUEUE_TTL_ORDER_CANCEL.name)
                // 到期后要转发的交换机
                .withArgument("x-dead-letter-exchange", QueueEnum.QUEUE_ORDER_DEAD_CANCEL.exchange)
                // 到期后转发的routeKey
                .withArgument("x-dead-letter-routing-key", QueueEnum.QUEUE_ORDER_DEAD_CANCEL.routeKey)
                // 设置消息过期时间 单位ms
                .withArgument("x-message-ttl", 9000)
                .build();
    }

    // 会员订单死信队列与交换机绑定
    @Bean
    public Binding memberBinding(DirectExchange memberDirExchange, Queue memberDeadQueue) {
        return BindingBuilder.bind(memberDeadQueue).to(memberDirExchange).with(QueueEnum.QUEUE_ORDER_DEAD_CANCEL.routeKey);
    }

    // 会员订单延迟队列与交换机绑定
    @Bean
    public Binding memberTTLBinding(DirectExchange memberDirExchange, Queue memberTTLQueue) {
        return BindingBuilder.bind(memberTTLQueue).to(memberDirExchange).with(QueueEnum.QUEUE_TTL_ORDER_CANCEL.routeKey);
    }

}
