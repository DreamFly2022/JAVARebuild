package com.hef.demo.hessian.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.hef.demo.api.OrderService;
import com.hef.demo.api.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import java.net.MalformedURLException;

/**
 * @Date 2022/2/16
 * @Author lifei
 */
@Configuration
public class BeanConfig {

    private final String USER_SERVICE_URL = "http://localhost:8088/userService";
    private final String ORDER_SERVICE_URL = "http://localhost:8088/orderService";

    /**
     * 第一种创建代理服务的方法
     * @return
     */
//    @Bean
//    public HessianProxyFactoryBean userService() {
//        HessianProxyFactoryBean factoryBean = new HessianProxyFactoryBean();
//        factoryBean.setServiceUrl(USER_SERVICE_URL);
//        factoryBean.setServiceInterface(UserService.class);
//        return factoryBean;
//    }


    /**
     * 第二种创建代理服务的方法
     * @return
     * @throws MalformedURLException
     */
    @Bean
    public UserService userService() throws MalformedURLException {
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        return (UserService) hessianProxyFactory.create(UserService.class, USER_SERVICE_URL);
    }


    @Bean
    public OrderService orderService() throws MalformedURLException {
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        return (OrderService) hessianProxyFactory.create(OrderService.class, ORDER_SERVICE_URL);
    }

}
