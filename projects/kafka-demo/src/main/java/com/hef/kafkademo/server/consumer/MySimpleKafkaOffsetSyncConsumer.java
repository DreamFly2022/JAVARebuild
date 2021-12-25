package com.hef.kafkademo.server.consumer;

import com.alibaba.fastjson.JSON;
import com.hef.kafkademo.bean.Order;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * 同步提交
 * @Date 2021/12/11
 * @Author lifei
 */
public class MySimpleKafkaOffsetSyncConsumer {

    public static void main(String[] args) {
        MySimpleKafkaOffsetSyncConsumer consumer = new MySimpleKafkaOffsetSyncConsumer();
        consumer.consumerPoll();
    }


    public void consumerPoll() {
        Properties props = new Properties();
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        // kafka group id 的配置
        props.setProperty("group.id", "group1");
        // 从头开始消费数据
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 同步提交： 1. 关闭自动提交； 2. 调用同步提交  consumer.commitSync();
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        String topic = "test32";
        //  构建一个Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(1000));
            poll.forEach(item->{
                ConsumerRecord<String, String> record = (ConsumerRecord) item;
                Order order = JSON.parseObject(record.value(), Order.class);
                System.out.println(order);
            });
            consumer.commitSync();
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
