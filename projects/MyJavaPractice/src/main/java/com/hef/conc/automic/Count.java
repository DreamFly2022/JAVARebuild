package com.hef.conc.automic;


/**
 * @Date 2021/4/12
 * @Author lifei
 */
public class Count {

    private int num = 0;

    public int add() {
        return ++num;
    }

    public int getNum() {
        return num;
    }
}
