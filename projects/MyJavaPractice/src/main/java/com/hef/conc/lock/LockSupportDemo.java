package com.hef.conc.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * @Date 2021/4/11
 * @Author lifei
 */
public class LockSupportDemo {

    public static Object u = new Object();

    private static ChangeObjectThread t1 = new ChangeObjectThread("t1");
    private static ChangeObjectThread t2 = new ChangeObjectThread("t2");

    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name) {super(name);}

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in " + getName());
                LockSupport.park();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("被中断了");
                }
                System.out.println(Thread.currentThread().getName()+"继续执行");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(1000L);
        t2.start();
        Thread.sleep(3000L);
        // interrupt 也能够让被ThreadSupport.park()的线程释放锁
        t1.interrupt();
        LockSupport.unpark(t2);
        t1.join();
        t2.join();

    }

}
