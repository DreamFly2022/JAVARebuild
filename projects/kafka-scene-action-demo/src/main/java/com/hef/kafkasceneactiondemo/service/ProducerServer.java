package com.hef.kafkasceneactiondemo.service;

import com.hef.kafkasceneactiondemo.bean.Order;

public interface ProducerServer {

    void createOrder(Order order);

}
