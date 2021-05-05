package com.hef.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
@Aspect
public class Aop2 {

    // 定义切点
    @Pointcut(value = "execution(* com.hef.aop.*.*ding(..))")
    public void point() {
    }


    @Before(value = "point()")
    public void before() {
        System.out.println(">>>>> aop2 before advice run.....");
    }

    @After(value = "point()")
    public void after() {
        System.out.println(">>> aop2 after advice run...");
    }

    @Around(value = "point()")
    public void round(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println(">>> aop2 round begin....");
        proceedingJoinPoint.proceed();
        System.out.println(">>> aop2 round end!!!");

    }
}
