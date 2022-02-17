package com.hef.demo.thrift.server;

import com.hef.demo.thrift.api.Order;
import com.hef.demo.thrift.api.OrderService;
import org.apache.thrift.TException;

/**
 * @Date 2022/2/17
 * @Author lifei
 */
public class OrderServiceImpl implements OrderService.Iface {
    @Override
    public Order findOrder(int id) throws TException {
        return new Order(id, "orderThrift", 0.3*id);
    }
}
