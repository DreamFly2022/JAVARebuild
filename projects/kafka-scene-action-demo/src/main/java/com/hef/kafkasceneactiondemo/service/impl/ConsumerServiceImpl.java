package com.hef.kafkasceneactiondemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.hef.kafkasceneactiondemo.bean.Order;
import com.hef.kafkasceneactiondemo.conf.MyDataConf;
import com.hef.kafkasceneactiondemo.service.ConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理订单
 * @Date 2021/12/25
 * @Author lifei
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Resource
    private KafkaConsumer<String, String> kafkaConsumer;

    @Resource
    private MyDataConf myDataConf;

    @Override
    public List<Order> handleOrder() {
        List<Order> orderList = new ArrayList<>();
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1));
        for (ConsumerRecord<String, String> record : records) {
            Order order = JSON.parseObject(record.value(), Order.class);
            orderList.add(order.clone());
        }
        kafkaConsumer.commitSync();
        return orderList;
    }

    @Override
    public List<Order> handleOrder(Long offset) {
        List<Order> orderList = new ArrayList<>();
        if (offset!=null && offset>0) {
            kafkaConsumer.seek(new TopicPartition(myDataConf.getProducerTopic(), 0), offset);
        }
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100l));
        for (ConsumerRecord<String, String> record : records) {
            Order order = JSON.parseObject(record.value(), Order.class);
            orderList.add(order.clone());
        }
        kafkaConsumer.commitSync();
        return orderList;
    }


}
