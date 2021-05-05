package com.hef.conc.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class CopyOnWriteArrayListDemo3 {

    private static List<String> list = new CopyOnWriteArrayList<>();

    /**
     * 这个例子再次证明，多步操作，不能保证原子性
     * list.size() 获得到的数，再次使用 list时， 可能已经变了
     * @param args
     */
    public static void main(String[] args) {
        test();
    }

    /*
    有人发博客说，这个代码能证明copyOnWriteArrayList也不是线程安全的。
    这完全是概念上的错误。
     */
    public static void test() {
        for (int i = 0; i < 10000; i++) {
            list.add("string" + i);
        }

        new Thread(()->{
            while (true) {
                if (list.size()>0) { // 下一个get操作是，list.size() 可能已经是0
                    String content = list.get(list.size() - 1);
                }
            }
        }).start();

        new Thread(()->{
            while (true) {
                if (list.size()<=0) {
                    break;
                }
                list.remove(0);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
