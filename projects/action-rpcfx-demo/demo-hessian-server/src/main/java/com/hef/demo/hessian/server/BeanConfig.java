package com.hef.demo.hessian.server;

import com.hef.demo.hessian.api.OrderService;
import com.hef.demo.hessian.api.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

/**
 * @Date 2022/2/16
 * @Author lifei
 */
@Configuration
public class BeanConfig {

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean("/userService")
    public HessianServiceExporter iniHessianService(UserService userService) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(UserService.class);
        exporter.setService(userService);
        return exporter;
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl();
    }


    @Bean("/orderService")
    public HessianServiceExporter iniHessianOrderService(OrderService orderService) {
        HessianServiceExporter proxyExporter = new HessianServiceExporter();
        proxyExporter.setService(orderService);
        proxyExporter.setServiceInterface(OrderService.class);
        return proxyExporter;
    }


}
