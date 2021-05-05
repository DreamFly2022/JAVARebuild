package com.hef.conc.collection;

import java.util.*;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class SyncListDemo {

    public static void main(String[] args) {
        List<Integer> list01 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 7);
        list01.set(7, 8);
//        list01.add(9);// throw an error
        System.out.println(list01);

        //  可以正常操作
        List<Integer> list02 = new ArrayList<>();
        list02.addAll(list01);
        System.out.println(list02);
        List<Integer> list03 = Collections.synchronizedList(list02);

        System.out.println(Arrays.toString(list03.toArray()));

        Collections.shuffle(list03);
        System.out.println(Arrays.toString(list03.toArray()));

        List<Integer> list04 = Collections.unmodifiableList(list02);
        System.out.println(list04.getClass());
//        list04.set(8, 10);
//        list04.add(11);

    }
}
