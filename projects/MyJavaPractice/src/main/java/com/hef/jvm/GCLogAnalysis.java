package com.hef.jvm;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author lifei
 * @since 2021/3/1
 */
public class GCLogAnalysis {

    private static Random random = new Random();

    public static void main(String[] args) {
        // 当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        // 持续运行毫秒数，可以根据需要进行修改
        long timeoutMillis = TimeUnit.SECONDS.toMillis(1);
        // 结束时间戳
        long endMillis = startMillis + timeoutMillis;
        LongAdder counter = new LongAdder();
        System.out.println("正在执行...");
        // 缓存一部分对象进入老年代
        int cacheSize = 2000;
        Object[] cacheGarbage = new Object[cacheSize];
        // 在时间范围内持续循环
        while (System.currentTimeMillis() < endMillis){
            Object garbage = generateGarbage(100 * 1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            // 有些对象被使用，有些对象没有被使用
            if (randomIndex<cacheSize){
                cacheGarbage[randomIndex] = garbage;
            }
        }
        System.out.println("对象个数：" + counter.toString());

    }

    private static Object generateGarbage(int max){
        int randomSize = random.nextInt(max);
        int type = randomSize % 4;
        Object garbage = null;
        switch (type){
            case 0:
                garbage = new int[randomSize];
                break;
            case 1:
                garbage = new byte[randomSize];
                break;
            case 2:
                garbage = new double[randomSize];
                break;
            default:
                StringBuffer sb = new StringBuffer();
                String randomString = "randomString-Anything";
                while (sb.length()<randomSize){
                    sb.append(randomString);
                    sb.append(max);
                    sb.append(randomSize);
                }
                garbage = sb.toString();
                break;
        }
        return garbage;
    }
}
