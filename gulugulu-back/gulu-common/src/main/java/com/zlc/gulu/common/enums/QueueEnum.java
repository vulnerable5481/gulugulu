package com.zlc.gulu.common.enums;

/*
 *   消息通知队列枚举
 * */
public enum QueueEnum {
    /*
     * 消息通知队列
     * */
    QUEUE_ORDER_DEAD_CANCEL("order.direct", "order.cancel.queue", "order.cancel"),
    /**
     * 消息通知ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("order.direct", "order.delay.queue", "order.delay");


    public String exchange;
    public String name;
    public String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
