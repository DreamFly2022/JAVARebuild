package com.hef.mypagesearch.bean;

/**
 * @Date 2022/1/10
 * @Author lifei
 */
public class OrderPageParam {

    private int beginRowNum;

    private int pageSize;

    public OrderPageParam() {
    }

    public OrderPageParam(int beginRowNum, int pageSize) {
        this.beginRowNum = beginRowNum;
        this.pageSize = pageSize;
    }

    public int getBeginRowNum() {
        return beginRowNum;
    }

    public void setBeginRowNum(int beginRowNum) {
        this.beginRowNum = beginRowNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
