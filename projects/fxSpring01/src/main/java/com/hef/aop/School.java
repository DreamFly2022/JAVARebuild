package com.hef.aop;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class School implements ISchool{


    @Override
    public void ding() {
        System.out.println("This is a School Run");
    }
}
