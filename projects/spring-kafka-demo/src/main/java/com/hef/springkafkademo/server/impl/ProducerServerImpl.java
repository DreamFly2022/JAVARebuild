package com.hef.springkafkademo.server.impl;

import com.alibaba.fastjson.JSON;
import com.hef.springkafkademo.conf.MyDataConf;
import com.hef.springkafkademo.domain.Order;
import com.hef.springkafkademo.server.ProducerServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
@Service
public class ProducerServerImpl implements ProducerServer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private MyDataConf myDataConf;

    @Override
    public void createOrder(String orderName) {
        if (StringUtils.isBlank(orderName)) return;
        Random random = new Random();
        Order order = new Order(orderName, Double.parseDouble(String.format("%.2f", random.nextDouble() * 100)));
        send(myDataConf.getProducerTopic(), JSON.toJSONString(order));
    }

    private void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
