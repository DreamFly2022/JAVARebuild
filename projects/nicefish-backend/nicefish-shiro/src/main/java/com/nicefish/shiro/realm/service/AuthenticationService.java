package com.nicefish.shiro.realm.service;

import com.nicefish.shiro.AuthException;

import java.util.List;
import java.util.Set;

/**
 * This interface defines three method to fetch password/rolenames/permissions for usertoken authentication.
 * @author kimmking (kimmking@163.com)
 * @date 2013-12-5
 */
public interface AuthenticationService {

    /**
     * @param username
     * @return the password of the user
     * @throws Exception
     */
    SaltedPassword findSaltedPasswordByUserName(String username) throws AuthException;

    /**
     * @param username
     * @return the roleName collection of the user
     * @throws Exception
     */
    Set<String> findRoleNamesForUserName(String username) throws AuthException;

    /**
     * @param username
     * @param roleNames
     * @return the permission string collection of the user
     * @throws Exception
     */
    Set<String> findPermissions(String username, List<String> roleNames) throws AuthException;

}