package com.hef.springkafkademo.domain;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
public class Order {

    private String orderName;
    private Double price;

    public Order() {
    }

    public Order(String orderName, Double price) {
        this.orderName = orderName;
        this.price = price;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderName='" + orderName + '\'' +
                ", price=" + price +
                '}';
    }
}
