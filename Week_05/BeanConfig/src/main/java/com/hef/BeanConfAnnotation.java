package com.hef;

import com.hef.service.RunService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lifei
 * @since 2020/11/18
 */
public class BeanConfAnnotation {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans-annotation.xml");
        RunService runService = applicationContext.getBean(RunService.class);
        runService.running();
    }
}
