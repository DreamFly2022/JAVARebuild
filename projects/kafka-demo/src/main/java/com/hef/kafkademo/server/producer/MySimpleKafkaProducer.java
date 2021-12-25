package com.hef.kafkademo.server.producer;

import com.alibaba.fastjson.JSON;
import com.hef.kafkademo.bean.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.math.BigDecimal;
import java.util.Properties;

/**
 * 极简的生产者
 * @Date 2021/12/11
 * @Author lifei
 */
public class MySimpleKafkaProducer {

    public static void main(String[] args) {
        MySimpleKafkaProducer producerMain = new MySimpleKafkaProducer();
        producerMain.producerSend();
    }

    public void producerSend() {
        Properties props = new Properties();
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        String topic = "test32";

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        // 发送消息
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setAmount(new BigDecimal(1000+i));
            order.setId(i);
            order.setType(System.currentTimeMillis());
            String value = JSON.toJSONString(order);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record);
        }
        producer.close();
    }
}
