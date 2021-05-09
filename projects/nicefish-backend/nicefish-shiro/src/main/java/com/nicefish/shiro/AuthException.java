package com.nicefish.shiro;

/**
 * Created by kimmking on 17/7/6.
 */
public class AuthException extends RuntimeException {

    public AuthException(){}

    public AuthException(Exception ex){
        super(ex);
    }

}
