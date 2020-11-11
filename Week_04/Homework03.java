package com.hef.concurrency;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * @author lifei
 * @since 2020/11/11
 */
public class Homework03 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // 方法一： FutureTask
        /*FutureTask<Integer> task = new FutureTask<>(()->{return fiboGood(36);});
        new Thread(task).start();
        Integer result = null;
        try {
            result = task.get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }*/

        // 方法二：FutureTask 使用线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(() -> {
            return fiboGood(36);
        });
        Integer result = null;
        try {
            result = future.get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }finally {
            // 注意一定要shutdown， 否则线程不会结束
            executorService.shutdown();
        }

//        int result01 = fibo(36); // 71ms
//        int result02 = fiboGood(36); // 1ms
        System.out.println("异步计算结果为：" + result);
//        System.out.println("异步计算result01结果为：" + result01);
//        System.out.println("异步计算result02结果为：" + result02);
        System.out.println("使用时间" + (System.currentTimeMillis() - startTime) + "ms");

    }


    private static int sum(){
        return fibo(36);
    }

    private static int fibo(int a){
        if (a<2){
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    /**
     * 对上面算法对优化
     * */
    private static int fiboGood(int a){
        if (a<2){
            return 1;
        }
        int result = 0;
        for (int i = 0, x=1, y=1; i < a-1; i++) {
            result = x + y;
            x = y;
            y = result;
        }
        return result;
    }
}
