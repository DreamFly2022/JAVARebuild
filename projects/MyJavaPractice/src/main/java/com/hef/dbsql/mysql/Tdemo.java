package com.hef.dbsql.mysql;

/**
 * @Date 2021/7/25
 * @Author lifei
 */
public class Tdemo {

    private Integer tId;
    private String tName;

    public Tdemo(Integer tId, String tName) {
        this.tId = tId;
        this.tName = tName;
    }

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    @Override
    public String toString() {
        return "Tdemo{" +
                "tId=" + tId +
                ", tName='" + tName + '\'' +
                '}';
    }
}
