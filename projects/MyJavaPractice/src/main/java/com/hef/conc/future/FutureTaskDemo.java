package com.hef.conc.future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class FutureTaskDemo {

    public static void main(String[] args) {
        // 第一种方式
        FutureTask<Integer> futureTask1 = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt();
            }
        });
//        new Thread(futureTask1).start();

        // 第二种方式
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(futureTask1);
        executorService.shutdown();


        try {
            System.out.println("result: " + futureTask1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
