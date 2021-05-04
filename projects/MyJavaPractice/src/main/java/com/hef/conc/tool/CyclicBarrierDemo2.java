package com.hef.conc.tool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class CyclicBarrierDemo2 {

    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N);
        for (int i = 0; i < N; i++) {
            new Thread(new Writer(cyclicBarrier)).start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("cyclicBarrier 重用");

        for (int i = 0; i < N; i++) {
            new Thread(new Writer(cyclicBarrier)).start();
        }


    }

    private static class Writer implements Runnable{

        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            System.out.println("线程" +Thread.currentThread().getName() + "正在写入数据...");
            try {
                Thread.sleep(3000);
                System.out.println("线程" + Thread.currentThread().getName()+"，写入数据完毕，等待其它线程写入数据完毕");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName()+"所有线程写入数据完毕，继续处理其它业务");
        }
    }
}
