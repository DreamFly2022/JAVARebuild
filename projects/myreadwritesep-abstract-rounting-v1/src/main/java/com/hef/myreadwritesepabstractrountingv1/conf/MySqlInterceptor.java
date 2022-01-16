package com.hef.myreadwritesepabstractrountingv1.conf;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
public class MySqlInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        System.out.println(args);

        return invocation.proceed();
    }
}
