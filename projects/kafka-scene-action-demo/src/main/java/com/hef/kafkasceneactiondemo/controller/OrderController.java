package com.hef.kafkasceneactiondemo.controller;

import com.hef.kafkasceneactiondemo.bean.Order;
import com.hef.kafkasceneactiondemo.service.ConsumerService;
import com.hef.kafkasceneactiondemo.service.ProducerServer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
@RestController
@RequestMapping(value = "/orderController")
public class OrderController {

    @Resource
    private ProducerServer producerServer;

    @Resource
    private ConsumerService consumerService;

    /**
     * 创建一个订单
     * @param symbol
     * @return
     */
    @RequestMapping(value = "/createOrder/{id}/{symbol}", method = RequestMethod.GET)
    public String createOrder(@PathVariable("id") Long id, @PathVariable("symbol") String symbol) {
        try {
            Random random = new Random();
            double v = Double.parseDouble(String.format("%.2f", random.nextDouble() * 100));
            Order order = new Order.Builder()
                    .id(id).ts(1000 + id)
                    .symbol(symbol).price(v).builder();
            producerServer.createOrder(order);
            return "success";
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return "fail";
        }
    }

    @RequestMapping(value = "/handleOrders", method = RequestMethod.GET)
    public List<Order> handleOrders() {
        try {
            List<Order> orderList = consumerService.handleOrder();
            return orderList;
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "/handleOrdersWithOffset/{offset}", method = RequestMethod.GET)
    public List<Order> handleOrdersWithOffset(@PathVariable("offset") Long offset) {
        try {
            List<Order> orderList = consumerService.handleOrder(offset);
            return orderList;
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
