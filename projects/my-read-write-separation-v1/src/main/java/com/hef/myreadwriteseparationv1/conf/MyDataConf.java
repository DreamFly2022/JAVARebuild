package com.hef.myreadwriteseparationv1.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 2022/1/15
 * @Author lifei
 */
@Configuration
public class MyDataConf{

    @Value("${mydata.mysql.url-write}")
    private String urlWrite;
    @Value("${mydata.mysql.url-read01}")
    private String urlRead01;
    @Value("${mydata.mysql.url-read02}")
    private String urlRead02;
    @Value("${mydata.mysql.username}")
    private String username;
    @Value("${mydata.mysql.password}")
    private String password;

    public String getUrlWrite() {
        return urlWrite;
    }

    public String getUrlRead01() {
        return urlRead01;
    }

    public String getUrlRead02() {
        return urlRead02;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
