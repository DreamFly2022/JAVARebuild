package com.hef;

import com.hef.aop.ISchool;
import com.hef.aop.Klass02;
import com.hef.aop.School;
import com.hef.spring01.Klass;
import com.hef.spring01.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Date 2021/5/5
 * @Author lifei
 */
public class RunMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student01 = context.getBean("student01", Student.class);
        Student student02 = context.getBean("student02", Student.class);
        System.out.println(student01);
        System.out.println(student02);

        Klass klass = context.getBean("klass", Klass.class);
        System.out.println(klass);

        Klass02 klass02 = context.getBean("klass02", Klass02.class);
        System.out.println(klass02);
        System.out.println("Klass对象AOP代理后的实际类型： " + klass02.getClass());
        System.out.println("Klass对象AOP代理后实际类型是否是Klass子类； " + (klass02 instanceof Klass02));
        klass02.dong();

        // 因为School被代理了， 这样引用会报错
//        School school = context.getBean(School.class);

        ISchool school = context.getBean(ISchool.class);
        System.out.println(school);
        System.out.println("School对象AOP代理后的实际类型： " + school.getClass());
        System.out.println("School对象AOP代理后实际类型是否是school子类； " + (school instanceof School));
        school.ding();


        // 查看Spring容器中加载了哪些Bean
        System.out.println("查看Spring容器中加载了哪些Bean: ");
        System.out.println("context.getBeanDefinitionNames() =====>" + String.join(", ", context.getBeanDefinitionNames()));


    }
}
