package com.hef.service.impl;

import com.hef.service.OrderService;

/**
 * @Date 2022/1/19
 * @Author lifei
 */
public class AppleOrderService implements OrderService {
    @Override
    public void seeOrder() {
        System.out.println("apple order...");
    }
}
