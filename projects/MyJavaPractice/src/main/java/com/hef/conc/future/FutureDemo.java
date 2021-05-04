package com.hef.conc.future;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class FutureDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            return new Random().nextInt();
        });
        executorService.shutdown();

        try {
            System.out.println("result: " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
