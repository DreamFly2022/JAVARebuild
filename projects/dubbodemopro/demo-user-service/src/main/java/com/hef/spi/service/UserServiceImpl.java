package com.hef.spi.service;

import com.hef.demo.spi.api.UserService;

/**
 * @Date 2022/3/2
 * @Author lifei
 */
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello() {
        return "Hello SPI user service";
    }
}
