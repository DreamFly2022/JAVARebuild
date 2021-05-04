package com.hef.conc.lock;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class ReentrantLockDemo {

    public static void main(String[] args) {
        final Count count = new Count();
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                count.get();
            }).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(()->{count.put();}).start();
        }
    }
}
