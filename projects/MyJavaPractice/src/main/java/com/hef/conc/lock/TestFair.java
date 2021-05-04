package com.hef.conc.lock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class TestFair {

    public static volatile int race = 0;
    /**
     公平锁要维护一个队列，后来的线程要加锁，即使锁空闲，也要先检查有没有其他线程在 wait，如果有自己要挂起，加到队列后面，然后唤醒队列最前面的线程。这种情况下相比较非公平锁多了一次挂起和唤醒
     线程切换的开销，其实就是非公平锁效率高于公平锁的原因，因为非公平锁减少了线程挂起的几率，后来的线程有一定几率逃离被挂起的开销
     */
    // // 改成false会好100倍
    public static ReentrantLock lock = new ReentrantLock(false);
    // java.util.concurrent.locks.ReentrantLockts = 9165
    // java.util.concurrent.locks.ReentrantLockts = 153
    public static void increase() {
        lock.lock();
        race++;
        lock.unlock();
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) {
        int count = Thread.activeCount();
        long now = System.currentTimeMillis();
        System.out.println(count);
        AtomicReference<Thread> sign = new AtomicReference<>();
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(()->{
                for (int j = 0; j < 100000; j++) {
                    increase();
                }
            });
            threads[i].start();
        }
        // 等待所有累加线程都结束
        while (Thread.activeCount()>count) {
            Thread.yield();
        }
        System.out.println(lock.getClass().getName() + "ts = " + (System.currentTimeMillis()-now));
    }

}
