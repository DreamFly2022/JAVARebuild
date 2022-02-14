package com.hef.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hef.rpcfx.api.RpcfxRequest;
import com.hef.rpcfx.api.RpcfxResolver;
import com.hef.rpcfx.api.RpcfxResponse;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Date 2022/2/15
 * @Author lifei
 */
public class RpcfxInvoker {

    private RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * 服务端执行
     * @param rpcfxRequest
     * @return
     */
    public RpcfxResponse invoker(RpcfxRequest rpcfxRequest) {
        RpcfxResponse rpcfxResponse = new RpcfxResponse();
        // 获取serviceName
        String serviceClass = rpcfxRequest.getServiceClass();
        // 获取具体的实现
        Object service = this.resolver.resolver(serviceClass);

        // 通过反射获取要执行的方法
        Method method = resolverMethodFromClass(service.getClass(), rpcfxRequest.getMethod());
        try {
            Object result = method.invoke(service, rpcfxRequest.getParams());
            // 对返回结果进行序列化
            rpcfxResponse.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            rpcfxResponse.setStatus(true);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // @TODO 封装一个统一的RpcFxException
            e.printStackTrace();
            rpcfxResponse.setException(e);
            rpcfxResponse.setStatus(false);
        }
        return rpcfxResponse;
    }

    /**
     * 通过反射获取要执行的方法
     * @param serviceClass
     * @param method
     * @return
     */
    private Method resolverMethodFromClass(Class<?> serviceClass, String method) {
        return Arrays.stream(serviceClass.getMethods())
                .filter(m-> StringUtils.equals(method, m.getName()))
                .findFirst().get();
    }
}
