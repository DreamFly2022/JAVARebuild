package com.hef.conc.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class NewFixedThreadExecutorDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(16);

        for (int i = 0; i < 10; i++) {
            final int no = i;
            executorService.execute(()->{
                System.out.println("start: " + no);
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end: " + no);
            });

        }
        executorService.shutdown();
        System.out.println("Main run End!");
    }
}
