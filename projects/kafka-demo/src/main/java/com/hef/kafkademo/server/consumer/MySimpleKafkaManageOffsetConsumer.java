package com.hef.kafkademo.server.consumer;

import com.alibaba.fastjson.JSON;
import com.hef.kafkademo.bean.Order;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

/**
 * 异步提交
 * @Date 2021/12/11
 * @Author lifei
 */
public class MySimpleKafkaManageOffsetConsumer {

    public static void main(String[] args) {
        MySimpleKafkaManageOffsetConsumer consumer = new MySimpleKafkaManageOffsetConsumer();
        consumer.consumerPoll();
    }


    public void consumerPoll() {
        Properties props = new Properties();
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        // kafka group id 的配置
        props.setProperty("group.id", "group6");
        // 从头开始消费数据
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 默认就是自动提交
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 设置时间，让其走批量提交
        props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        String topic = "test32";
        //  构建一个Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
            // 该方法会在再均衡开始之前和消费者停止读取之后被调用。
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                // 获取已经提交的偏移量
                Map<TopicPartition, OffsetAndMetadata> committed = consumer.committed(new HashSet<>(partitions));
                for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : committed.entrySet()) {
                    TopicPartition key = entry.getKey();
                    int partition = key.partition();
                    OffsetAndMetadata value = entry.getValue();
                    long offset = 0;
                    if (Objects.nonNull(value)) {
                        offset = value.offset();
                    }
                    System.out.println(String.format("partition: %d , offset: %d", partition, offset));
                }
            }

            // 该方法会在再均衡之后和消费者读取之前被调用
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                Map<TopicPartition, OffsetAndMetadata> committed = consumer.committed(new HashSet<>(partitions));
                // 设置偏移量
                for (TopicPartition partition : partitions) {
                    for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : committed.entrySet()) {
                        TopicPartition key = entry.getKey();
                        if (StringUtils.equals(key.topic(), partition.topic()) && key.partition()==partition.partition()) {
                            OffsetAndMetadata value = entry.getValue();
                            long nextOffset = 1;
                            if (Objects.nonNull(value)) {
                                nextOffset = value.offset()+1;
                            }

                            consumer.seek(partition, nextOffset);
                            System.out.println(String.format("topic: %s, partition: %d, offset: %d", partition.topic(), partition.partition(), nextOffset));
                            break;
                            // 测试
                            /*if (Objects.nonNull(entry.getValue()) && partition.partition()==0 && entry.getValue().offset()>=20) {
                                consumer.seek(partition, 19);
                            }
                            if (Objects.nonNull(entry.getValue()) && partition.partition()==1 && entry.getValue().offset()>=31) {
                                consumer.seek(partition, 33);
                            }
                            if (Objects.nonNull(entry.getValue()) && partition.partition()==2 && entry.getValue().offset()>=25) {
                                consumer.seek(partition, 27);
                            }*/
                        }
                    }
                }
            }
        });
        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(1000));
            poll.forEach(item->{
                ConsumerRecord<String, String> record = (ConsumerRecord) item;
                Order order = JSON.parseObject(record.value(), Order.class);
                System.out.println("record offset: ======> " + record.offset());
                System.out.println(order);
            });
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
