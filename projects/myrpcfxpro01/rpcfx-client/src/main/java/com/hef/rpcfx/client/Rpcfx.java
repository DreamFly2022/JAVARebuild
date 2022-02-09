package com.hef.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.hef.rpcfx.api.RpcfxRequest;
import com.hef.rpcfx.api.RpcfxResponse;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Date 2022/2/9
 * @Author lifei
 */
public class Rpcfx {

    /**
     * 通过动态代理创建 服务
     * @param serviceClass
     * @param url
     * @param <T>
     * @return
     */
    public static <T> T create(final Class<T> serviceClass, final String url) {
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass},
                new RpcfxInvocationHandler(serviceClass, url));
    }


    public static class RpcfxInvocationHandler implements InvocationHandler {

        private MediaType JSON_TYPE  = MediaType.parse("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;

        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            RpcfxRequest rpcfxRequest = new RpcfxRequest();
            rpcfxRequest.setClassName(this.serviceClass.getName());
            rpcfxRequest.setMethod(method.getName());
            rpcfxRequest.setParams(params);

            RpcfxResponse response = post(rpcfxRequest, url);
            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(final RpcfxRequest rpcfxRequest, final String url) throws IOException {
            String reqJson = JSON.toJSONString(rpcfxRequest);
            System.out.println("request json: " + reqJson);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSON_TYPE, reqJson))
                    .build();
            String responseBody = client.newCall(request).execute().body().string();
            System.out.println("response json: " + responseBody);
            return JSON.parseObject(responseBody, RpcfxResponse.class);
        }
    }
}
