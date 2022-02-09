package com.hef.rpcfx.client;

import com.alibaba.fastjson.parser.ParserConfig;
import com.hef.rpcfx.api.User;
import com.hef.rpcfx.api.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Date 2022/2/9
 * @Author lifei
 */
@SpringBootApplication
public class RpcfxClientApplication {

    // 为了fastJson反序列化的安全
    static {
        ParserConfig.getGlobalInstance().addAccept("com.hef");
    }

    public static void main(String[] args) {
        UserService userService = Rpcfx.create(UserService.class, "http://localhost:8088");
        User user = userService.findUser(1);
        System.out.println("find user id=1 from server: " + user.getName());
    }
}
