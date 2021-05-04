package com.hef.conc.tool;

import java.util.concurrent.CountDownLatch;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class CountDownLatchDemo {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new ReadNum(i, countDownLatch)).start();
        }
        //  跟CyclicBarrier不同，这里在主线程await
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==》各子线程执行结束……");
        System.out.println("==》主线称执行结束……");
    }

    private static class ReadNum implements Runnable {

        private int id;
        private CountDownLatch countDownLatch;

        public ReadNum(int id, CountDownLatch countDownLatch) {
            this.id = id;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println("id: " + id + ", " + Thread.currentThread().getName());
//                countDownLatch.countDown();
                System.out.println("线程任务" + id + "结束，其它任务继续");
                // 当任务完成之后，再countDown
                countDownLatch.countDown();
            }
        }
    }
}
