package com.hef.dbsql.mysql;

import java.util.Date;

/**
 * @Date 2021/7/25
 * @Author lifei
 */
public class OrderItem {

    private Long oiId;
    private String oid;
    private Long pid;
    private Integer pnum;
    private Date createTime;

    public OrderItem() {
    }

    public OrderItem(Long oiId, String oid, Long pid, Integer pnum, Date createTime) {
        this.oiId = oiId;
        this.oid = oid;
        this.pid = pid;
        this.pnum = pnum;
        this.createTime = createTime;
    }

    public Long getOiId() {
        return oiId;
    }

    public void setOiId(Long oiId) {
        this.oiId = oiId;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "oiId=" + oiId +
                ", oid='" + oid + '\'' +
                ", pid=" + pid +
                ", pnum=" + pnum +
                ", createTime=" + createTime +
                '}';
    }
}
