package com.hef.conc.tool;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("回调>>" + Thread.currentThread().getName());
                System.out.println("回调>>线程组执行结束");
                System.out.println("==》各子线程执行结束。。。");
            }
        });

        for (int i = 0; i < 5; i++) {
            new Thread(new RunNum(i, cyclicBarrier)).start();
        }
//        System.out.println("===》主线程执行结束。。。");

        // CyclicBarrier 可重复利用, CountDownLatch  做不到
        for (int i = 11; i < 16; i++) {
            new Thread(new RunNum(i, cyclicBarrier)).start();
        }

    }

    private static class RunNum implements Runnable {
        private int id;
        private CyclicBarrier cyclicBarrier;

        public RunNum(int id, CyclicBarrier cyclicBarrier) {
            this.id = id;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println("id: " + id + ", " + Thread.currentThread().getName());
                try {
//                    cyclicBarrier.await();
                    System.out.println("线程任务" + id + "结束，其它任务继续。");
                    // 注意： 跟CountDownLatch 不同，这里在子线程await
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
