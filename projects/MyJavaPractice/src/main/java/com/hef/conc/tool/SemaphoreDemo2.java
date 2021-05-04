package com.hef.conc.tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class SemaphoreDemo2 {

    private static final int threadCount = 20;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

//        final Semaphore semaphore = new Semaphore(5);
        final Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.execute(()->{
                try {
                    // 获取全部许可，退化成串行执行
                    semaphore.acquire(3);
                    test(threadNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // 释放多个许可
                    semaphore.release(3);
                }
            });
        }

        executorService.shutdown();
    }

    private static void test(int threadNum) {
        System.out.println("id: " + threadNum+ ", " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
