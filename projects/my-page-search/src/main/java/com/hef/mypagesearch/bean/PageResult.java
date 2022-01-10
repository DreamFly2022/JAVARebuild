package com.hef.mypagesearch.bean;

import java.util.List;

/**
 * @Date 2022/1/10
 * @Author lifei
 */
public class PageResult<T> {

    private List<T> result;
    /** 总共的记录数 */
    private int totalNum;

    /** 一页的记录数 */
    private int pageSize;

    /** 当前的页码数 */
    private int currentPageNum;

    /** 总共的页码数 */
    private int totalPageNum;

    /** 上一页的页码数 */
    private int prePageNum;

    /** 下一页的页码数 */
    private int nextPageNum;

    public PageResult(int totalNum, int pageSize, int currentPageNum) {
        this.totalNum = Math.max(0, totalNum);
        this.pageSize = Math.max(1, pageSize);
        this.totalPageNum = this.totalNum%this.pageSize==0?this.totalNum/this.pageSize:this.totalNum/this.pageSize+1;
        this.currentPageNum = Math.max(1, Math.min(currentPageNum, this.totalPageNum));
        this.prePageNum = Math.max(1, this.currentPageNum-1);
        this.nextPageNum = Math.min(this.totalPageNum, this.currentPageNum+1);
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

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

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getPrePageNum() {
        return prePageNum;
    }

    public void setPrePageNum(int prePageNum) {
        this.prePageNum = prePageNum;
    }

    public int getNextPageNum() {
        return nextPageNum;
    }

    public void setNextPageNum(int nextPageNum) {
        this.nextPageNum = nextPageNum;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "result=" + result +
                ", totalNum=" + totalNum +
                ", pageSize=" + pageSize +
                ", currentPageNum=" + currentPageNum +
                ", totalPageNum=" + totalPageNum +
                ", prePageNum=" + prePageNum +
                ", nextPageNum=" + nextPageNum +
                '}';
    }
}
