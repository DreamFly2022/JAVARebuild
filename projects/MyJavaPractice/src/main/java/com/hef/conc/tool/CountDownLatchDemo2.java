package com.hef.conc.tool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class CountDownLatchDemo2 {

    private final static int threadCount = 200;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.execute(()->{
                try{
                    test(threadNum);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("==》 所有程序员任务完成，项目顺利上线！");
        executorService.shutdown();
    }

    private static void test(int threadNum) {
        try {
            Thread.sleep(100l);
            System.out.println(String.format("程序员[%d]完成任务。。。", threadNum));
            Thread.sleep(100l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
