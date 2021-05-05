package com.hef.learn.springxbean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.stream.Stream;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class Test {

    /**
     * 1. 点击 maven 的 package;
     * 2. 之后spring.handlers 和 datasource.xsd 会自动生成
     * 3. 运行这个main方法，程序就能正常运行了
     * 参考： https://blog.christianposta.com/activemq/easier-way-to-create-custom-spring-config-namespaces-using-xbean-spring/
     *
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("test.xml");
        MyLFDataSoruce bean = context.getBean(MyLFDataSoruce.class);

        System.out.println(bean.getConfigurations().isEmpty());
        System.out.println(bean.getConfigurations().stream().findFirst().map(XRuleConfiguration::getType).get());
    }
}
