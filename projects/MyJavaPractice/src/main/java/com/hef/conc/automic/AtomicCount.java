package com.hef.conc.automic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class AtomicCount {

    private AtomicInteger num = new AtomicInteger();

    public int add() {
        return num.getAndIncrement();
    }

    public int getNum() {
        return num.get();
    }
}
