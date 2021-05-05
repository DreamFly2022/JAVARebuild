package com.hef.conc.threadloacl;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class ThreadLocalDemo {

    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public ThreadLocal<Integer> getSeqNum() {
        return seqNum;
    }

    public int getNextNum() {
        seqNum.set(seqNum.get() +1);
        return seqNum.get();
    }


    private static class SnThread extends Thread {
        private ThreadLocalDemo sn;
        public SnThread(ThreadLocalDemo sn) {
            this.sn = sn;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("Thread[" + Thread.currentThread().getName() + "]---sn [" + sn.getNextNum() + "]");
            }
            // 用完之后，一定remove掉
            sn.getSeqNum().remove();
        }
    }


    public static void main(String[] args) {
        ThreadLocalDemo demo = new ThreadLocalDemo();
        SnThread client1 = new SnThread(demo);
        SnThread client2 = new SnThread(demo);
        SnThread client3 = new SnThread(demo);

        client1.start();
        client2.start();
        client3.start();
    }
}
