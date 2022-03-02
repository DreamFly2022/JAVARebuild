package com.hef.spi.service;

import com.hef.demo.spi.api.UserService;

import java.util.ServiceLoader;

/**
 * @Date 2022/3/2
 * @Author lifei
 */
public class UserMain {

    public static void main(String[] args) {
        ServiceLoader<UserService> load = ServiceLoader.load(UserService.class);
        for (UserService userService : load) {
            System.out.println(userService.sayHello());
        }
    }
}
