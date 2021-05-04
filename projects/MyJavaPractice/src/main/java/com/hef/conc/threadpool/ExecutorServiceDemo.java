package com.hef.conc.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class ExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(16);

        try {
            String str = executorService.submit(()->{
                return "I am a task";
            }).get();
            System.out.println("str="+str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.println("Main run end!");
    }
}
