package com.hef.conc.automic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class LongDemo {

    public static void main(String[] args) {
        final AtomicLong atomicLong = new AtomicLong();
        final LongAdder longAdder = new LongAdder();

        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    atomicLong.getAndIncrement();
                    longAdder.increment();
                }
            }).start();
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("atomicLong=" + atomicLong.get());
        System.out.println("longAdder=" + longAdder.sum());
    }



}
