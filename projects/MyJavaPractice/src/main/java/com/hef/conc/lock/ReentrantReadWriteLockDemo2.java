package com.hef.conc.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class ReentrantReadWriteLockDemo2 {

    private final Map<String, Object> map = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    /**
     * 通过读写锁，可以对受保护的共享资源进行并发读取和独占写入。
     * 读写锁是可以在读取或写入模式下锁定的单一实体。要修改资源，线程必须首先获取互斥写锁。
     * 必须释放所有读锁之后，才允许使用互斥写锁
     * @param key
     * @return
     */
    public Object readWrite(String key) {
        Object value = null;
        System.out.println("1. 首先开启读锁去缓存中读取数据");
        lock.readLock().lock();
        try{
            value = map.get(key);
            if (value==null) {
                System.out.println("2. 数据不存在，释放读锁，开启写锁");
                lock.readLock().unlock();
                lock.writeLock().lock();
                try{
                    if (value==null) {
                        value="aaaa";
                    }
                }finally {
                    System.out.println("3. 释放写锁");
                    lock.writeLock().unlock();
                }
                System.out.println("4. 开启读锁");
                lock.readLock().lock();
            }
        }finally {
            System.out.println("释放读锁");
            lock.readLock().unlock();
        }
        return value;
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockDemo2 demo2 = new ReentrantReadWriteLockDemo2();
        Object result = demo2.readWrite("bingfabiancheng");
        System.out.println(result);
    }
}
