package com.hef.conc.lock;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Date 2021/4/11
 * @Author lifei
 */
public class ConditionDemo {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[20];

    int putpter, takepter, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try{
            // 当count等于数组大小时，当前线程等待，知道notFull通知，再进行生产
            while (count == items.length) {
                System.out.println("notFull await begin....");
                notFull.await();
                System.out.println("notFull await end....");
            }
            items[putpter] = x;
            if (++putpter == items.length) putpter = 0;
            ++count;
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try{
            // 当count=0， 进入等待，知道notEmpty通知，进行消费
            while (count==0) {
                System.out.println("notEmpty await begin....");
                notEmpty.await();
                System.out.println("notEmpty await end....");
            }
            Object x = items[takepter];
            if (++takepter == items.length) takepter=0;
            --count;
            notFull.signal();
            return x;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionDemo conditionDemo = new ConditionDemo();
        new Thread(()->{
            try {
                while (true) {
                    System.out.println("生产aa");
                    conditionDemo.put("aa");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                while (true) {
                    Object take = conditionDemo.take();
                    System.out.println("消费：" + take);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
