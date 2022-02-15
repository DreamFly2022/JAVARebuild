package com.hef.rpcfx.demo.provider;

import com.hef.demo.api.Order;
import com.hef.demo.api.OrderService;

/**
 * @Date 2022/2/15
 * @Author lifei
 */
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findOrder(int id) {
        return new Order(id, "aa", id*0.32f);
    }
}
