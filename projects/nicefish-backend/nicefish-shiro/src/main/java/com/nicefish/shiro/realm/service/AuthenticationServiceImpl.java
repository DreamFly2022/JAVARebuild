package com.nicefish.shiro.realm.service;

import com.nicefish.mapper.*;
import com.nicefish.model.User;
import com.nicefish.shiro.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by kimmking on 17/6/29.
 */

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public SaltedPassword findSaltedPasswordByUserName(String username) throws AuthException {
        try {
            User user = userMapper.findByUserName(username);
            SaltedPassword saltedPassword = new SaltedPassword();
            saltedPassword.setSalt(username);
            saltedPassword.setPassword(user.getPassword());
            return saltedPassword;
        } catch (RuntimeException ex) {
            throw new AuthException(ex);
        }
    }

    @Override
    public Set<String> findRoleNamesForUserName(String username) throws AuthException {
        try {
            return roleMapper.findRoleNamesForUserName(username);
        } catch (RuntimeException ex) {
            throw new AuthException(ex);
        }
    }

    @Override
    public Set<String> findPermissions(String username, List<String> roleNames) throws AuthException {
        try {
            return permissionMapper.findPermissions(roleNames);
        } catch (RuntimeException ex) {
            throw new AuthException(ex);
        }
    }
}
