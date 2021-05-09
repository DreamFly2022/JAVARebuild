package com.hef.java8;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 范型的示例
 * @Date 2021/5/9
 * @Author lifei
 */
public class GenericDemo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        Class clazz = demo.getClass();
        // 获得该类的父类
        System.out.println(clazz.getSuperclass());
        // 获得带有泛型的父类
        // Type 是Java编程语言中所有类型的公共高级接口,它们包括原始类型、参数化类型、数组类型、类型变量和基本类型
//        Type type01 = new int[];
        Type type = clazz.getGenericSuperclass();
        System.out.println();
        // ParameterizedType 参数化类型，即泛型
        ParameterizedType parameterizedType = (ParameterizedType) type;
        // 获取参数化类型的数组，泛型可能有多个
        // Class 的父类是type
        Class c = (Class) parameterizedType.getActualTypeArguments()[0];
        System.out.println(c);

    }

    public static class People<T> {

    }

    public static class Demo extends People<GenericDemo> {

    }
}
