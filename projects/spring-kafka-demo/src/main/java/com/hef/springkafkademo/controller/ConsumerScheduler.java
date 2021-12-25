package com.hef.springkafkademo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.hef.springkafkademo.domain.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
@Component
public class ConsumerScheduler {

    /**
     * 消费
     * @param message
     */
    @KafkaListener(topics = {"test32"}, groupId = "g01")
    public void pollOrder(@Payload String message) {
        JSONValidator from = JSONValidator.from(message);
        if (from.validate()) {
            Order order = JSON.parseObject(message, Order.class);
            System.out.println("消费到一个order： ==================>");
            System.out.println(order);
        }
    }
}
