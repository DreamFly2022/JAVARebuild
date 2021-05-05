package com.hef.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class Aop1 {

    // 环绕通知
    public void runAroundTest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("This around advice begin....");
        proceedingJoinPoint.proceed();
        System.out.println("This around advice end....");
    }

    // after
    public void afterTest() {
        System.out.println("This is after advice");
    }

    public void afterReturningTest() {
        System.out.println("This is after-returning advice");
    }

    public void beforeTest() {
        System.out.println(">>>>> This is before advice");
    }
}
