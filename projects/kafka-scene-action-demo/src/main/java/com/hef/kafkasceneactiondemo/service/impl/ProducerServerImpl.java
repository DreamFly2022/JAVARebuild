package com.hef.kafkasceneactiondemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.hef.kafkasceneactiondemo.bean.Order;
import com.hef.kafkasceneactiondemo.conf.MyDataConf;
import com.hef.kafkasceneactiondemo.service.ProducerServer;
import kafka.common.KafkaException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 生产者
 * @Date 2021/12/25
 * @Author lifei
 */
@Service
public class ProducerServerImpl implements ProducerServer {

    @Resource
    private KafkaProducer<String, String> producer;

    @Resource
    private MyDataConf myDataConf;

    @Override
    public void createOrder(Order order) {
        try {
            producer.beginTransaction();
            ProducerRecord<String, String> record = new ProducerRecord<>(myDataConf.getProducerTopic(), order.getId() + "", JSON.toJSONString(order));
            producer.send(record, ((metadata, exception) -> {
                if (exception!=null) {
                    producer.abortTransaction();
                    throw new KafkaException(exception.getMessage()+" ==> "+record);
                }
            }));
            // 同步提交
            producer.flush();
            producer.commitTransaction();
        }catch (Exception e) {
            producer.abortTransaction();
        }
    }
}
