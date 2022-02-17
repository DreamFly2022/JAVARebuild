package com.hef.demo.thrift.server;

import com.hef.demo.thrift.api.User;
import com.hef.demo.thrift.api.UserService;
import org.apache.thrift.TException;

/**
 * @Date 2022/2/17
 * @Author lifei
 */
public class UserServiceImpl implements UserService.Iface {
    @Override
    public User findUser(int id) throws TException {
        return new User(id, "thrift"+id);
    }
}
