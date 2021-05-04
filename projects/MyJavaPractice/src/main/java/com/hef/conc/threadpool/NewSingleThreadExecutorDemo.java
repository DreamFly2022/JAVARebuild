package com.hef.conc.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class NewSingleThreadExecutorDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int no = i;
            Runnable runnable = () ->{
                System.out.println("start: " + no);
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end: " + no);
            };
            executorService.execute(runnable);
        }
        executorService.shutdown();
        System.out.println("Main run end!");
    }
}
