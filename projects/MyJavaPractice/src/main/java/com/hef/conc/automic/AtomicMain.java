package com.hef.conc.automic;

/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class AtomicMain {

    public static void main(String[] args) {
        final SyncCount count = new SyncCount();
//        final AtomicCount count = new AtomicCount();
//        final Count count = new Count();
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    count.add();
                }}).start();
        }

        try {
            // 必须睡的事件足够长， 否则 syncCount 会有问题
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("num=" + count.getNum());
    }
}
