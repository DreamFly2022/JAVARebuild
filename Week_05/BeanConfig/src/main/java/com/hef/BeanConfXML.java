package com.hef;

import com.hef.domain.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lifei
 * @since 2020/11/18
 */
public class BeanConfXML {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Car car = applicationContext.getBean(Car.class);
        System.out.println(car);
    }
}
