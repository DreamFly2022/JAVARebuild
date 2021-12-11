package com.hef.kafkademo.bean;

import java.math.BigDecimal;

/**
 * @Date 2021/12/11
 * @Author lifei
 */
public class Order {

    private BigDecimal amount;
    private long id;
    private long type;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Order{" +
                "amount=" + amount +
                ", id=" + id +
                ", type=" + type +
                '}';
    }
}
