package com.hef.conc.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class ConcurrentHashMapDemo {

    public static void main(String[] args) {
        demo2();
    }

    public static void demo2() {
        final Map<String, AtomicInteger> count = new ConcurrentHashMap<>();
//        final Map<String, AtomicInteger> count = new HashMap<>();
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Runnable task = () -> {
            AtomicInteger oldValue;
            for (int i = 0; i < 5; i++) {
                oldValue = count.get("a");
                if (null == oldValue) {
                    AtomicInteger zero = new AtomicInteger(0);
//                    oldValue = count.putIfAbsent("a", zero);
                    oldValue = count.put("a", zero);
                    if (oldValue==null) {
                        oldValue = zero;
                    }

                }
                oldValue.incrementAndGet();
            }
            countDownLatch.countDown();
        };

        new Thread(task).start();
        new Thread(task).start();

        try {
            countDownLatch.await();
            System.out.println(count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
