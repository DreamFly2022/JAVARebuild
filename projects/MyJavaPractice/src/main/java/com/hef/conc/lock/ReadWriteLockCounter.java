package com.hef.conc.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Date 2021/5/4
 * @Author lifei
 */
public class ReadWriteLockCounter {

    private int sum = 0;
    //  可重入 + 读写锁 + 公平锁
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public int incrAndGet() {
        try {
            // 写锁， 独占锁， 被读锁排斥
            lock.writeLock().lock();
            return ++sum;
        }finally {
            lock.writeLock().unlock();
        }
    }

    public int getSum() {
        try {
            // 读锁， 共享锁，保证可见行
            lock.readLock().lock();
            return sum;
        }finally {
            lock.readLock().unlock();
        }
    }

}
