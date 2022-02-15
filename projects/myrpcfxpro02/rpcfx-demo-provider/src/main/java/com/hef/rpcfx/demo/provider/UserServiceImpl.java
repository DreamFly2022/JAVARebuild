package com.hef.rpcfx.demo.provider;

import com.hef.demo.api.User;
import com.hef.demo.api.UserService;

/**
 * @Date 2022/2/15
 * @Author lifei
 */
public class UserServiceImpl implements UserService {

    @Override
    public User findUser(int id) {
        return new User(id, "Frankeleyn");
    }

}
