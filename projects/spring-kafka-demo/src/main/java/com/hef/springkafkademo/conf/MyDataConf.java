package com.hef.springkafkademo.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
@Configuration
public class MyDataConf {

    @Value("${mydata.kafka.producer-topic}")
    private String producerTopic;

    public String getProducerTopic() {
        return producerTopic;
    }
}
