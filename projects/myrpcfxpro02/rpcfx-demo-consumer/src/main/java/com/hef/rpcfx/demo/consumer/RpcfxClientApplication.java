package com.hef.rpcfx.demo.consumer;

import com.hef.demo.api.Order;
import com.hef.demo.api.OrderService;
import com.hef.demo.api.User;
import com.hef.demo.api.UserService;
import com.hef.rpcfx.client.Rpcfx;

/**
 * @Date 2022/2/15
 * @Author lifei
 */
public class RpcfxClientApplication {

    public static void main(String[] args) {
        UserService userService = Rpcfx.create(UserService.class, "http://localhost:8088/");
        User user = userService.findUser(2);
        System.out.println(user);

        OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8088/");
        Order order = orderService.findOrder(2);
        System.out.println(order);
    }
}
