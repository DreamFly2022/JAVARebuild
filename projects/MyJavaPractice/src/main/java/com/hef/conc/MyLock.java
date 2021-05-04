package com.hef.conc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Date 2021/4/11
 * @Author lifei
 */
public class MyLock {

    public void createMyLock() {
        Lock lock = new ReentrantLock(true);

    }
}
