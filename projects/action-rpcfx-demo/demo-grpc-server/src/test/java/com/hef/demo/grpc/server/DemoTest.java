package com.hef.demo.grpc.server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/2/27
 * @Author lifei
 */
public class DemoTest {

    private final Map<String, List<Integer>> map = new HashMap<>();

    @Test
    public void putOrAbsentTest() {

        String[] keys = {"aa", "bb", "cc", "bb", "aa"};
        Integer[] values = {1, 2, 3, 4, 5};
        for (int i = 0; i < keys.length; i++) {
            List<Integer> nodes = nodeMapTest(keys[i]);
            nodes.add(values[i]);
            System.out.println(map);
        }
    }

    private List<Integer> nodeMapTest(String key) {
        List<Integer> defaultList = new ArrayList<>();
        List<Integer> nodes = map.putIfAbsent(key, defaultList);
        return nodes!=null?nodes:defaultList;
    }
}
