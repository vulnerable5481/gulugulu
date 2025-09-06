//package com.zlc.gulu.server.controller;
//
//import com.rabbitmq.client.Channel;
//import com.zlc.gulu.common.enums.QueueEnum;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/order")
//public class OrderController {
//
//    /*
//     * 购买会员
//     * */
////    @PostMapping("/member")
////    public Result payMember(){
////        return orderService.createMemberOrder();
////    }
//
//    // 生成会员订单
//    createMemberOrder(MemberDTO memberDTO) {
//        // 责任链校验信息
//
//        // 生成订单
//
//        // 保存订单到数据库
//
//        // 将订单消息添加到延迟队列中【我思来想去还是把订单当做消息，而不是单纯的订单ID，减少数据库交互】
//        rabbitTemplate.converAndSend(
//                QueueEnum.QUEUE_TTL_ORDER_CANCEL.exchange,
//                QueueEnum.QUEUE_TTL_ORDER_CANCEL.routeKey,
//                memberOrderDO);
//    }
//
//    // 支付订单
//    @RabbitListener(queues = "gulu.order.queue")
//    payMemberOrder(MemberOrder memberOrder) {
//        // 支付操作
//    }
//
//    // 超时取消订单
//    cancelMemberOrder(MemberOrder memberOrder) {
//
//        // 查询数据库中订单的最新状态，确认没有被支付
//
//        // 释放商品库存
//
//        // 返回优惠券
//
//        // 数据库删除订单
//    }
//
//    /*
//     * 写一个取消订单的监听器
//     * */
//    @RabbitListener(queues = "order.cancel.queue")
//    @Service
//    public class OrderCloseListener() {
//        @RabbitHandler
//        public void listener(MemberOrder memberOrder, Channel channel, Message message) throws IOException {
//            try {
//                // 取消订单
//                orderService.cancelMemberOrder(memberOrder);
//                // 删除消息
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            } catch (Exception e) {
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
//            }
//        }
//    }
//
//}
