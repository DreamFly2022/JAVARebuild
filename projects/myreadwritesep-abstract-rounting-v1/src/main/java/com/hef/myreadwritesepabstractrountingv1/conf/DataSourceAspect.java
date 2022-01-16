package com.hef.myreadwritesepabstractrountingv1.conf;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);


    /**
     * 切点
     */
    @Pointcut("@annotation(com.hef.myreadwritesepabstractrountingv1.conf.CurDataSource)")
    public void dataSourcePointCut() {
    }

    /**
     * 增强
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        CurDataSource annotation = method.getAnnotation(CurDataSource.class);
        if (annotation==null) {
            DynamicDataSource.setDataSource(DataSourceKey.WRITE_KEY);
            LOGGER.info("使用默认数据库连接： " + DataSourceKey.WRITE_KEY);
        }else {
            DynamicDataSource.setDataSource(annotation.name());
            LOGGER.info("使用指定数据库连接： " + annotation.name());
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
        }
    }
}
