package com.hef.mypagesearch.bean;

/**
 * @Date 2022/1/10
 * @Author lifei
 */
public class PageParam {

    // 一页查询的记录数，默认为10
    private int pageSize = 10;

    // 当前的页数
    private int currentPageNum;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }
}
