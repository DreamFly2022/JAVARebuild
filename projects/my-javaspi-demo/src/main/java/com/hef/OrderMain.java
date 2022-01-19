package com.hef;

import com.hef.service.OrderService;

import java.util.ServiceLoader;

/**
 * @Date 2022/1/19
 * @Author lifei
 */
public class OrderMain {

    public static void main(String[] args) {
        ServiceLoader<OrderService> load = ServiceLoader.load(OrderService.class);
        for (OrderService orderService : load) {
            orderService.seeOrder();
        }
    }
}
