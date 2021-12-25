package com.hef.springkafkademo.controller;

import com.hef.springkafkademo.domain.ResResult;
import com.hef.springkafkademo.server.ProducerServer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
@RestController
@RequestMapping(value = "/orderController")
public class OrderController {

    @Resource
    private ProducerServer producerServer;

    /**
     * 测试
     * @return
     */
    @RequestMapping(value = "/findOneResult", method = RequestMethod.GET)
    public ResResult<String> findOneResult() {
        return new ResResult<>("oneResult", 0, "success");
    }

    /**
     * 创建一个订单
     * 在浏览器上调用即可
     * @param orderName
     * @return
     */
    @RequestMapping(value = "/createOrder/{orderName}", method = RequestMethod.GET)
    public ResResult<String> createOrder(@PathVariable("orderName") String orderName) {
        try {
            producerServer.createOrder(orderName);
            return new ResResult<>("create OK", 0, "success");
        }catch (Exception e) {
            return new ResResult<>(null, 1, e.getMessage());
        }
    }
}
