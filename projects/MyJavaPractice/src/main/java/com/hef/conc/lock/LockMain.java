package com.hef.conc.lock;

/**
 * 死锁的案例
 * @Date 2021/4/11
 * @Author lifei
 */
public class LockMain {

    public static void main(String[] args) {
        Count3 count3 = new Count3();
        ThreadA threadA = new ThreadA(count3);
        threadA.setName("线程A");
        threadA.start();
        ThreadB threadB = new ThreadB(count3);
        threadB.setName("线程B");
        threadB.start();
    }
}
