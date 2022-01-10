package com.hef.mypagesearch.service;

import com.hef.mypagesearch.bean.OrderItem;
import com.hef.mypagesearch.bean.PageParam;
import com.hef.mypagesearch.bean.PageResult;

public interface OrderService {

    /**
     * 分页查询
     * @param pageParam
     * @return
     */
    PageResult<OrderItem> findPageOrder(PageParam pageParam);
}
