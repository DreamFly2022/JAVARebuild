package com.hef.conc.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 异常是否能够在主线程中捕获到
 * @Date 2021/4/12
 * @Author lifei
 */
public class ExceptionDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        //  能够捕获到异常
        try {
            Future<Double> future = executorService.submit(() -> {
                throw new RuntimeException("executorService.submit()");
            });
            Double b = future.get();
            System.out.println(b);
        }catch (Exception e) {
            System.out.println("catch submit");
            e.printStackTrace();
        }

        // 无法捕获到异常
        /*try {
            executorService.execute(() -> {
                throw new RuntimeException("executorService.submit()");
            });
        }catch (Exception e) {
            System.out.println("catch execute");
            e.printStackTrace();
        }*/

        executorService.shutdown();
        System.out.println("Main Thread run..");

    }
}
