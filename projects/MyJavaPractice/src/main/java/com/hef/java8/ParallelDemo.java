package com.hef.java8;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 并行和串行的区别
 * @Date 2021/5/9
 * @Author lifei
 */
public class ParallelDemo {

    public static void main(String[] args) {
        int n = 500_0000;
        List<Object> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(UUID.randomUUID().toString());
        }
        System.out.println("开始排序");
        // 纳秒， 比毫秒精度高
        long t01 = System.nanoTime();
//        list.stream().sorted();
        sortTest(list, (x)->{x.stream().sorted();});  // 串行Stream耗时： 58毫秒
//        sortTest(list, (x)->{x.parallelStream().sorted();}); // 串行Stream耗时： 50毫秒
        long t02 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t02 - t01);
        System.out.println("串行Stream耗时： " + millis  + "毫秒");

    }


    private static void sortTest(List<Object> list, Consumer<List<Object>> sortM) {
        sortM.accept(list);
    }
}
