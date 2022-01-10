package com.hef.mypagesearch.service.impl;

import com.hef.mypagesearch.bean.OrderItem;
import com.hef.mypagesearch.bean.OrderPageParam;
import com.hef.mypagesearch.bean.PageParam;
import com.hef.mypagesearch.bean.PageResult;
import com.hef.mypagesearch.dao.OrderItemDao;
import com.hef.mypagesearch.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Date 2022/1/10
 * @Author lifei
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderItemDao orderItemDao;


    @Override
    public PageResult<OrderItem> findPageOrder(PageParam pageParam) {
        // 查询页数
        int totalCount = orderItemDao.findOrderItemTotalCount();
        PageResult<OrderItem> pageResult = new PageResult<>(totalCount, pageParam.getPageSize(),
                pageParam.getCurrentPageNum());
        int beginRowNum = (pageResult.getCurrentPageNum()-1) * pageResult.getPageSize();
        List<OrderItem> itemList = orderItemDao.findPageOrderItemList(new OrderPageParam(beginRowNum, pageResult.getPageSize()));
        pageResult.setResult(itemList);
        return pageResult;
    }
}
