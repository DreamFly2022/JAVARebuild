package com.hef.java8;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2021/5/9
 * @Author lifei
 */
public class StreamDemo2 {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(7, 1,2,2, 3,4,5);
        System.out.println(list);

        Optional<Integer> first = list.stream().findFirst();
        System.out.println(first.map((x)-> x*100).orElse(1000));

        int sum = list.stream().filter(x->x<3).distinct().reduce(10, (x, y)->x+y);
        System.out.println(sum);

        // 用法一： 两个参数， 分别代表 map的key 和value。 这样写会有问题， 因为map的key不能重复，一旦这里出现重复的key 就会报错。
        Map<Integer, Integer> map01 = list.stream().distinct()
                .collect(Collectors.toMap(a -> a, (a) -> a + 1));
        System.out.println(map01);
        // 用法二： 四个参数，解决key重复的问题： 前两个参数分别代表 key 和 value， 第三个参数代表 如果key重复如何处理， 第四个参数定义返回的数据类型
        Map<Integer, Integer> map02 = list.parallelStream()
                .collect(Collectors.toMap(a -> a, (a) -> a + 1, (a, b)-> a, LinkedHashMap::new));
        System.out.println(map02);


    }
}
