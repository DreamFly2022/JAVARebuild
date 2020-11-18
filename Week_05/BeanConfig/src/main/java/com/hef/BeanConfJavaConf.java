package com.hef;

import com.hef.config.BeanConfig;
import com.hef.domain.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lifei
 * @since 2020/11/18
 */
public class BeanConfJavaConf {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);
        Car dreamCar = applicationContext.getBean(Car.class);
        System.out.println(dreamCar);
    }
}
