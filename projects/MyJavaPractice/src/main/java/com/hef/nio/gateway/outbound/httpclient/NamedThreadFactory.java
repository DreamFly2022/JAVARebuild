package com.hef.nio.gateway.outbound.httpclient;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String namePrefix;
    private final boolean daemon;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup threadGroup;

    public NamedThreadFactory(String namePrefix, boolean daemon){
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = securityManager==null?
                Thread.currentThread().getThreadGroup():securityManager.getThreadGroup();
    }

    public NamedThreadFactory(String namePrefix){
        this(namePrefix, false);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r, namePrefix + "-thread-" + threadNumber.getAndIncrement(), 0);
        t.setDaemon(daemon);
        return t;
    }
}
