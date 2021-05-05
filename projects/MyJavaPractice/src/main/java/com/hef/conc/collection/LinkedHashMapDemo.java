package com.hef.conc.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class LinkedHashMapDemo {

    public static void main(String[] args) {
        // test hash map
        System.out.println("===> 1. Test HashMap");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("name1", "value1");
        hashMap.put("name2", "value2");
        hashMap.put("name3", "value3");

        for(Map.Entry<String, String> entry: hashMap.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }

        // test linked hash Map
        // 两种顺序： 1. 按照操作的顺序（LinedHashMap）； 2。 元素在集合中是有序的 （TreeMap）；
        System.out.println("===> 2. test linked hashMap");
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("name1", "value1");
        linkedHashMap.put("name2", "value2");
        linkedHashMap.put("name3", "value3");
        for(Map.Entry<String, String> entry: linkedHashMap.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }

    }
}
