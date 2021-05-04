package com.hef.conc.tool;

import java.util.concurrent.Semaphore;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class SemaphoreDemo3 {

    static Warehouse buf = new Warehouse();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            // 生产者
            new Thread(new Producer()).start();
            // 消费者
            new Thread(new Consumer()).start();
        }
    }

    static class Producer implements Runnable{

        static int num = 1;

        @Override
        public void run() {
            int n = num++;
            while (true) {
                buf.put(n);
                System.out.println(">" + n);
                try {
                    // 消息10毫秒
                    Thread.sleep(10l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                System.out.println("<" + buf.take());
                //速度较快，消息1000毫秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Warehouse{
        // 非满锁
        private Semaphore notFull = new Semaphore(10);
        // 非空锁
        private Semaphore notEmpty = new Semaphore(0);
        // 核心锁
        private Semaphore mutex = new Semaphore(1);
        // 库存容量
        final Object[] items = new Object[10];
        int putter, taker, count;

        public void put(Object o) {
            try {
                notFull.acquire();
                mutex.acquire();
                items[putter] = o;
                if (++putter == items.length) {
                    putter = 0;
                }
                ++count;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                mutex.release();
                notEmpty.release();
            }
        }

        public Object take() {
            try {
                notEmpty.acquire();
                mutex.acquire();
                Object o = items[taker];
                if (++taker == items.length) {
                    taker = 0;
                }
                --count;
                return o;
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally {
                mutex.release();
                notFull.release();
            }
        }
    }
}
