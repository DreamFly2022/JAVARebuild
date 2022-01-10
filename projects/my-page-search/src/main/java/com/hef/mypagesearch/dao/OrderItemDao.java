package com.hef.mypagesearch.dao;

import com.hef.mypagesearch.bean.OrderItem;
import com.hef.mypagesearch.bean.OrderPageParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemDao {

    int findOrderItemTotalCount();

    /**
     * 查询一页数据
     * @param orderPageParam
     * @return
     */
    List<OrderItem> findPageOrderItemList(OrderPageParam orderPageParam);
}
