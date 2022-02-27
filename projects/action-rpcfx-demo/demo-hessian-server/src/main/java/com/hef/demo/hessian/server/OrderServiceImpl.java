package com.hef.demo.hessian.server;

import com.hef.demo.hessian.api.Order;
import com.hef.demo.hessian.api.OrderService;

/**
 * @Date 2022/2/16
 * @Author lifei
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrder(int id) {
        return new Order(id, "apple hef", id*0.3f);
    }
}
