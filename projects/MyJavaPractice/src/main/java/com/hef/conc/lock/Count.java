package com.hef.conc.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class Count {

    // 可重入的公平锁
    final Lock lock = new ReentrantLock(true);

    public void get() {
        try{
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " get begin ");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " get end ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void put() {
        try{
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " put begin ");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " put end ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
