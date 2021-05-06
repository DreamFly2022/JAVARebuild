package com.hef.springjms;

import com.hef.spring01.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lifei
 * @since 2021/5/6
 */
public class DemoJmsSender {

    public static void main(String[] args) {
        Student student = new Student(22, "springJMS");
        ApplicationContext context = new ClassPathXmlApplicationContext("springjms-sender.xml");
        SendService sendService = (SendService) context.getBean("sendService");
        sendService.send(student);

        System.out.println("send successfully, please visit http://localhost:8161/admin to see it");
    }
}
