package com.hef.rpcfx.demo.provider;

import com.hef.demo.api.OrderService;
import com.hef.demo.api.UserService;
import com.hef.rpcfx.api.RpcfxResolver;
import com.hef.rpcfx.server.RpcfxInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 2022/2/15
 * @Author lifei
 */
@Configuration
public class BeanConfig {

    @Bean("com.hef.demo.api.UserService")
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean("com.hef.demo.api.OrderService")
    public OrderService orderService() {
        return new OrderServiceImpl();
    }

    @Bean
    public RpcfxResolver rpcfxResolver() {
        return new DemoResolver();
    }

    @Bean
    public RpcfxInvoker rpcfxInvoker(RpcfxResolver rpcfxResolver) {
        return new RpcfxInvoker(rpcfxResolver);
    }
}
