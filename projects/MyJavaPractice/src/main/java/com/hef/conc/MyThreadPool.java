package com.hef.conc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @Date 2021/4/11
 * @Author lifei
 */
public class MyThreadPool {

    public ThreadPoolExecutor initThreadPool(){
        int coreSize = Runtime.getRuntime().availableProcessors();
        int maxSize = Runtime.getRuntime().availableProcessors() * 2;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(500);
        CustomThreadFactory customThreadFactory = new CustomThreadFactory();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, maxSize, 1,
                TimeUnit.MINUTES, queue, customThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor;
    }

    public void exector01(){
        Executors.newSingleThreadExecutor();
        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool(10);
        Executors.newScheduledThreadPool(10);
    }

    class CustomThreadFactory implements ThreadFactory {
        private AtomicInteger seial = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(false);
            thread.setName("CustomeThread-" + seial.getAndIncrement());
            return thread;
        }
    }
}
