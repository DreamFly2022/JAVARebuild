package com.hef.kafkademo.server.producer;

import com.alibaba.fastjson.JSON;
import com.hef.kafkademo.bean.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * 异步发送
 * @Date 2021/12/11
 * @Author lifei
 */
public class MySimpleKafkaASyncProducer {

    public static void main(String[] args) {
        MySimpleKafkaASyncProducer producerMain = new MySimpleKafkaASyncProducer();
        producerMain.producerSend();
    }

    public void producerSend() {
        Properties props = new Properties();
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        String topic = "test32";

        // 异步发送的优化
        // 批量发送
        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "10240");
        // 实践限定
        props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");


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
                // 直接就是异步发送
                producer.send(record);
            }
            Thread.sleep(3000);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            producer.close();
        }
    }
}
