package com.hef.conc.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class LockCounter {

    private int sum = 0;

    // 可重入 + 公平锁
    private Lock lock = new ReentrantLock(true);

    public int addAndGet() {
        try {
            lock.lock();
            return ++sum;
        }finally {
            lock.unlock();
        }
    }

    public int getSum() {
        return sum;
    }


    public static void main(String[] args) {
        int loopNum = 100_0000;
        LockCounter lockCounter = new LockCounter();
        IntStream.range(0, loopNum).parallel().forEach((i)->{
            lockCounter.addAndGet();
        });
        System.out.println(lockCounter.getSum());
    }
}
