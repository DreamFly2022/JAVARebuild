package com.hef.conc.automic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class SyncCount {

    private int num = 0;
    private Lock lock = new ReentrantLock(true);

    public int add() {
        lock.lock();
        try{
            return num++;
        }finally {
            lock.unlock();
        }
    }

    public int getNum() {
        return num;
    }
}
