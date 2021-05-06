package com.hef.springjms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Date 2021/5/6
 * @Author lifei
 */
public class JmsReceiver {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("springjms-receiver.xml");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("receive successfully, please visit http://localhost:8161/admin to see it");
    }
}
