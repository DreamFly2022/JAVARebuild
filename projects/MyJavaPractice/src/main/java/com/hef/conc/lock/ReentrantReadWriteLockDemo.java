package com.hef.conc.lock;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class ReentrantReadWriteLockDemo {

    /**
     * 读写锁： 读锁不互斥，写锁互斥
     * @param args
     */
    public static void main(String[] args) {
        final Count2 count = new Count2();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                count.get();
            }).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(()->{count.put();}).start();
        }
    }
}
