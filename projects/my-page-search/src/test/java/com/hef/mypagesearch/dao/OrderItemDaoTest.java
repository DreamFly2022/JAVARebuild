package com.hef.mypagesearch.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Date 2022/1/10
 * @Author lifei
 */
@SpringBootTest
public class OrderItemDaoTest {

    @Resource
    private OrderItemDao orderItemDao;

    @Test
    public void findOrderItemTotalCountTest() {
        int result = orderItemDao.findOrderItemTotalCount();
        System.out.println(result);

    }
}
