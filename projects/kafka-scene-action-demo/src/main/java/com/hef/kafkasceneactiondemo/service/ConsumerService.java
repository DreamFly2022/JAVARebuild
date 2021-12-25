package com.hef.kafkasceneactiondemo.service;

import com.hef.kafkasceneactiondemo.bean.Order;

import java.util.List;

public interface ConsumerService {

    List<Order> handleOrder();
    List<Order> handleOrder(Long offset);
}
