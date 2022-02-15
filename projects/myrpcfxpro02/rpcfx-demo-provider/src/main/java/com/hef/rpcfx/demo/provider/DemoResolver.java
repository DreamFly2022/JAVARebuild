package com.hef.rpcfx.demo.provider;

import com.hef.rpcfx.api.RpcfxResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Date 2022/2/15
 * @Author lifei
 */
public class DemoResolver implements RpcfxResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object resolver(String serviceClass) {
        return this.applicationContext.getBean(serviceClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
