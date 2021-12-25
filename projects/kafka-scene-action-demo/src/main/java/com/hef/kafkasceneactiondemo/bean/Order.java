package com.hef.kafkasceneactiondemo.bean;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
public class Order implements Cloneable{

    private Long id;
    private Long ts;
    private String symbol;
    private Double price;

    public Order(){}
    private Order(Builder builder){
        this.id = builder.id;
        this.ts = builder.ts;
        this.symbol = builder.symbol;
        this.price = builder.price;
    }

    public static class Builder{
        private Long id;
        private Long ts;
        private String symbol;
        private Double price;

        public Builder id(Long id) {this.id = id; return this;}
        public Builder ts(Long ts) {this.ts = ts; return this;}
        public Builder symbol(String symbol) {this.symbol = symbol; return this;}
        public Builder price(Double price) {this.price = price; return this;}

        public Order builder() {
            return new Order(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public Order clone()  {
        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", ts=" + ts +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                '}';
    }
}
