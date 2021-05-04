package com.hef.conc.lock;

/**
 * @Date 2021/4/11
 * @Author lifei
 */
public class ThreadB extends Thread{

    Count3 count3;

    public ThreadB(Count3 count3) {
        this.count3 = count3;
    }

    @Override
    public void run() {
        count3.lockMethod();
    }
}
