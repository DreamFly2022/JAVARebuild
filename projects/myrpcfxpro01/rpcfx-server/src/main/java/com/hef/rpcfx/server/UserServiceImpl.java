package com.hef.rpcfx.server;

import com.hef.rpcfx.api.User;
import com.hef.rpcfx.api.UserService;

/**
 * @Date 2022/2/9
 * @Author lifei
 */
public class UserServiceImpl implements UserService {


    @Override
    public User findUser(int id) {
        return new User(id, "Frankeleyn");
    }
}
