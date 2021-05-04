package com.hef.conc.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class Count2 {

    // 可重入的公平锁
    final ReentrantReadWriteLock lock =  new ReentrantReadWriteLock(true);

    public void get() {
        try{
            lock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + " get begin ");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " get end ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void put() {
        try{
            lock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + " put begin ");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " put end ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
