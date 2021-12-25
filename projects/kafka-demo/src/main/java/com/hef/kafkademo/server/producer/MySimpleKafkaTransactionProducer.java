package com.hef.kafkademo.server.producer;

import com.alibaba.fastjson.JSON;
import com.hef.kafkademo.bean.Order;
import kafka.common.KafkaException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * 顺序发送
 * @Date 2021/12/11
 * @Author lifei
 */
public class MySimpleKafkaTransactionProducer {

    public static void main(String[] args) {
        MySimpleKafkaTransactionProducer producerMain = new MySimpleKafkaTransactionProducer();
        producerMain.producerSend();
    }

    public void producerSend() {
        Properties props = new Properties();
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        String topic = "test32";

        // 1. acks = all； 2. 幂等 true (设置了true，会自动把acks设置为all)； 3.设置事务Id
        props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx0001");
        // 可是设置超时时间
//        props.setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "360000");


        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        try {
            producer.initTransactions();
            producer.beginTransaction();
            // 发送消息
            for (int i = 1; i < 3; i++) {
                Order order = new Order();
                order.setAmount(new BigDecimal(1000 + i));
                order.setId(i);
                order.setType(System.currentTimeMillis());
                String value = JSON.toJSONString(order);
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
                // 测试
//                int v = 1/0;
                // 直接就是异步发送
                producer.send(record, (metadata, e)->{
                    if (e!=null) {
                        producer.abortTransaction();
                        throw new KafkaException(e.getMessage()+" ,data " + record);
                    }
                });
            }
            producer.commitTransaction();
            Thread.sleep(3000);
        }catch (Exception e) {
            producer.abortTransaction();
            throw new RuntimeException(e);
        }finally {
            producer.close();
        }
    }
}
