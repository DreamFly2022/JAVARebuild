package com.hef.conc.collection;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class TreeMapDemo {

    public static void main(String[] args) {
        Map<Integer, String> treeMap = new TreeMap<>(Comparator.reverseOrder());
        treeMap.put(3, "val");
        treeMap.put(2, "val");
        treeMap.put(1, "val");
        treeMap.put(4, "val");
        treeMap.put(5, "val");
        treeMap.put(6, "val");

        System.out.println(treeMap);

        Map<Integer, String> treeMap1 = new TreeMap<>(Comparator.naturalOrder());
        treeMap1.putAll(treeMap);
        System.out.println(treeMap1);
    }
}
