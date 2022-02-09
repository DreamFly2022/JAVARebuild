package com.hef.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hef.rpcfx.api.RpcfxRequest;
import com.hef.rpcfx.api.RpcfxResponse;
import com.hef.rpcfx.api.User;
import com.hef.rpcfx.api.UserService;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

/**
 * @Date 2022/2/9
 * @Author lifei
 */
@SpringBootApplication
@RestController
public class RpcfxServerApplication implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(RpcfxServerApplication.class, args);
    }


    @GetMapping("/test")
    public String test() {
        return "aa";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RpcfxResponse call(@RequestBody RpcfxRequest rpcfxRequest) {
        String serviceName = rpcfxRequest.getClassName();
        // @TODO 实践1： 改成范型和反射
        UserService userService = (UserService) this.applicationContext.getBean(serviceName);
        int id = Integer.parseInt(rpcfxRequest.getParams()[0].toString());
        User user = userService.findUser(id);
        RpcfxResponse rpcfxResponse = new RpcfxResponse();
        rpcfxResponse.setResult(JSON.toJSONString(user, SerializerFeature.WriteClassName));
        rpcfxResponse.setStatus(true);
        return rpcfxResponse;
    }


    @Bean("com.hef.rpcfx.api.UserService")
    public UserService userService() {
        UserService userService = new UserServiceImpl();
        return userService;
    }


}
