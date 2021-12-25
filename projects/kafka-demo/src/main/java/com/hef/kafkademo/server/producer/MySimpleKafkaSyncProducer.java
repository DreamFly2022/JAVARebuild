package com.hef.kafkademo.server.producer;

import com.alibaba.fastjson.JSON;
import com.hef.kafkademo.bean.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 极简的生产者
 * @Date 2021/12/11
 * @Author lifei
 */
public class MySimpleKafkaSyncProducer {

    public static void main(String[] args) {
        MySimpleKafkaSyncProducer producerMain = new MySimpleKafkaSyncProducer();
        producerMain.producerSend();
    }

    public void producerSend() {
        Properties props = new Properties();
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        String topic = "test32";


        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        try {
            // 发送消息
            for (int i = 0; i < 10; i++) {
                Order order = new Order();
                order.setAmount(new BigDecimal(1000 + i));
                order.setId(i);
                order.setType(System.currentTimeMillis());
                String value = JSON.toJSONString(order);
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
                Future<RecordMetadata> future = producer.send(record);
                // 同步发送方法一：
//                RecordMetadata recordMetadata = future.get();
//                System.out.println(recordMetadata);
//                System.out.println("发送消息： "+i);
                // 同步发送方法二：
                producer.flush();
            }
            Thread.sleep(3000);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            producer.close();
        }
    }
}
