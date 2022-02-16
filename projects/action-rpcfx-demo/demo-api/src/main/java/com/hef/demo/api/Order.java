package com.hef.demo.api;

import java.io.Serializable;

/**
 * @Date 2022/2/16
 * @Author lifei
 */
public class Order implements Serializable {

    private int id;
    private String name;
    private float amount;

    public Order() {
    }

    public Order(int id, String name, float amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
