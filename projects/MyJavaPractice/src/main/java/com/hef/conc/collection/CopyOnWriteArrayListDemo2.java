package com.hef.conc.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class CopyOnWriteArrayListDemo2 {

    private static final int THREAD_POOL_MAX_NUM = 10;
    private List<String> list = new ArrayList<>(); // 不能运行
//    private List<String> list = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        new CopyOnWriteArrayListDemo2().start();
    }

    private void start() {
        initData();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_MAX_NUM);
        for (int i = 0; i < THREAD_POOL_MAX_NUM; i++) {
            executorService.execute(new ReadList(list));
            executorService.execute(new WriteList(i, list));
        }
        executorService.shutdown();
    }

    private void initData(){
        for (int i = 0; i < THREAD_POOL_MAX_NUM; i++) {
            this.list.add("....line" + (i+1) + "......");
        }
    }

    private static class ReadList implements Runnable {

        private List<String> list;
        public ReadList(List<String> list) {
            this.list = list;
        }

        @Override
        public void run() {
            if (list!=null) {
                for (String v : this.list) {
                    System.out.println(Thread.currentThread().getName() + " : " + v);
                }
            }
        }
    }

    private static class WriteList implements Runnable {

        private int index;
        private List<String> list;
        public WriteList(int index, List<String> list) {
            this.index = index;
            this.list = list;
        }

        @Override
        public void run() {
            if (this.list!=null) {
//                this.list.remo ve(index);
                this.list.add(".....add" + index + "....");
            }
        }
    }
}
