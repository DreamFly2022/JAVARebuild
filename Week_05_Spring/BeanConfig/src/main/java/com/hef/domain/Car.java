package com.hef.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author lifei
 * @since 2020/11/18
 */
public class Car {

    private String name;
    private String color;
    private Double price;

    public Car(){}
    public Car(Builder builder){
        this.name = builder.name;
        this.color = builder.color;
        this.price = builder.price;
    }

    public static class Builder{
        private String name;
        private String color;
        private Double price;

        public Builder name(String name){ this.name = name;return this; }
        public Builder color(String color){ this.color = color;return this; }
        public Builder price(Double price){ this.price = price;return this; }

        public Car builder(){
            return new Car(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
