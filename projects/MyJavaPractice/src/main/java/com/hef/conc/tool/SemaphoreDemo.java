package com.hef.conc.tool;

import java.util.concurrent.Semaphore;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        // 工人数
        int N = 8;
        // 机器数量
//        Semaphore semaphore = new Semaphore(5);
        // 如果改成 1 ， 则变成单线程串行执行
        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < N; i++) {
            new Worker(i, semaphore).start();
        }
    }

    static class Worker extends Thread {
        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人 " + num + "占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人 " + num + "释放出机器");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        }
    }
}
