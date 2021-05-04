package com.hef.conc.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class NewScheduledThreadExecutorDemo {

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(16);
        for (int i = 0; i < 100; i++) {
            final int no = i;
            Runnable runnable = ()->{
                try {
                    System.out.println("start: " + no);
                    Thread.sleep(1000L);
                    System.out.println("end: " + no);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            // 10s后执行
//            System.out.println("--");
            executorService.schedule(runnable, 10l, TimeUnit.SECONDS);
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }
}
