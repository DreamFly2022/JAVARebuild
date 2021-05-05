package com.hef.conc.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class CopyOnWriteArrayListDemo1 {

    public static void main(String[] args) {
        // ArrayList, LinkedList, Vector 不安全，运行报错
        // 为什么Vector不安全？
//        List<Integer> list = new ArrayList<>();
//        List<Integer> list = new LinkedList<>();
        // vector 中，读是线程安全的，写是线程安全的，但读和写加在一起就不是线程安全的了。
//        List<Integer> list = new Vector<>();
        // 只有当使用 CopyOnWriteArrayList 的时候才不会报错
        List<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        T1 t01 =  new T1(list);
        T2 t02 =  new T2(list);
        t01.start();
        t02.start();
    }

    public static class T1 extends Thread {
        private List<Integer> list;

        public T1(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (Integer v : list) {

            }
        }
    }

    public static class T2 extends Thread {
        private List<Integer> list;
        public T2(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                list.remove(i);
            }
        }
    }
}
