package com.hef.mypagesearch.controller;

import com.hef.mypagesearch.bean.OrderItem;
import com.hef.mypagesearch.bean.PageParam;
import com.hef.mypagesearch.bean.PageResult;
import com.hef.mypagesearch.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Date 2022/1/10
 * @Author lifei
 */
@RestController
@RequestMapping(value = "/orderController")
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/findPageOrder", method = RequestMethod.POST)
    public PageResult<OrderItem> findPageOrder(@RequestBody PageParam pageParam) {
        return orderService.findPageOrder(pageParam);
    }
}
