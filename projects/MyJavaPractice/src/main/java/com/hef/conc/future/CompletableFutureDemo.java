package com.hef.conc.future;

import java.util.concurrent.CompletableFuture;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {
        // 1. 变换结果
        System.out.println("1。 变换结果");
        // 在一个任务上继续加任务
        String result1 = CompletableFuture.supplyAsync(() -> {
            return "hello";
        }).thenApplyAsync(v -> v + " world").join();
        System.out.println(result1);

        // 2. 消费
        CompletableFuture.supplyAsync(()->{return "hello";}).thenAccept(v->{
            System.out.println("消费");
            System.out.println("consumer: " + v);
        });

        // 3 组合， 方式一
        System.out.println("===组合");
        String result3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        }), (s1, s2) -> {
            return s1 + " " + s2;
        }).join();
        System.out.println("thenCombine: " + result3);
        // 组合方式二： 和第一种方式的区别是传递参数的个数不同
        CompletableFuture.supplyAsync(()->"Hello, learning java")
                .thenApply(String::toUpperCase)
                .thenCompose(s->CompletableFuture.supplyAsync(s::toLowerCase))
                .thenAccept(v->{
                    System.out.println("thenCompose: " + v);
                });

        // 4. 竞争
        System.out.println("竞争");
        String result4 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        }), (s) -> s).join();
        System.out.println(result4);

        // 5. 补偿异常
        String result5 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (true) {
                throw new RuntimeException("exception test!");
            }
            return "Hi, boy!";
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "Hello world";
        }).join();
        System.out.println(result5);
    }
}
