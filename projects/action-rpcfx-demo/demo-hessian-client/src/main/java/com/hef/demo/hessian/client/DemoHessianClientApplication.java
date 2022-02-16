package com.hef.demo.hessian.client;

import com.hef.demo.api.Order;
import com.hef.demo.api.OrderService;
import com.hef.demo.api.User;
import com.hef.demo.api.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Date 2022/2/16
 * @Author lifei
 */
@SpringBootApplication
@RestController
public class DemoHessianClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoHessianClientApplication.class, args);
    }

    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/userTest", method = RequestMethod.GET)
    public User userTest() {
        System.out.println(userService);
        User user = userService.findUser(23);
        System.out.println(user);
        return user;
    }

    @RequestMapping(value = "/orderTest", method = RequestMethod.GET)
    public Order orderTest() {
        Order order = orderService.findOrder(1);
        System.out.println(order);
        return order;
    }
}
