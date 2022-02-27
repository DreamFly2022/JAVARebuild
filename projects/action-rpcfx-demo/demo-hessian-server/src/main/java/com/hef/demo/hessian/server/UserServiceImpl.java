package com.hef.demo.hessian.server;

import com.hef.demo.hessian.api.User;
import com.hef.demo.hessian.api.UserService;

/**
 * @Date 2022/2/16
 * @Author lifei
 */
public class UserServiceImpl implements UserService {

    @Override
    public User findUser(int id) {
        return new User(id, "hessian "+id);
    }
}
