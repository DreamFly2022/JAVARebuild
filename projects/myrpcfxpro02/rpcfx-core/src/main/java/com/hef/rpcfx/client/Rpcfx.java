package com.hef.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
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
 * 创建代理对象
 * @Date 2022/2/15
 * @Author lifei
 */
public class Rpcfx {

    // 为了反序列化的安全
    static {
        ParserConfig.getGlobalInstance().addAccept("com.hef");
    }

    /**
     * 创建代理对象
     * @param serviceClass
     * @param url
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> serviceClass, String url) {
        return (T)Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));
    }

    /**
     * 动态代理的具体实现
     */
    public static class RpcfxInvocationHandler implements InvocationHandler {

        private final Class<?> serviceClass;
        private final String url;
        //
        private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

        public <T> RpcfxInvocationHandler(final Class<T> serviceClass, final String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            // 封装请求参数
            RpcfxRequest rpcfxRequest = new RpcfxRequest();
            rpcfxRequest.setServiceClass(this.serviceClass.getName());
            rpcfxRequest.setMethod(method.getName());
            rpcfxRequest.setParams(params);
            // 请求远程服务
            RpcfxResponse rpcfxResponse = post(rpcfxRequest, url);
            return JSON.parse(rpcfxResponse.getResult().toString());
        }

        /**
         * 通过OKHttpClient 请求远程服务
         * @param rpcfxRequest
         * @param url
         * @return
         */
        public RpcfxResponse post(RpcfxRequest rpcfxRequest, String url) throws IOException {
            // 对请求参数序列化
            String reqJson = JSON.toJSONString(rpcfxRequest);
            System.out.println("reqJson:" + reqJson);
            // 创建OkHttpClient
            // @TODO 尝试使用httpClient 或nettyClient
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSON_TYPE, reqJson))
                    .build();
            String respJson = client.newCall(request).execute().body().string();
            System.out.println("respJson: " + respJson);
            return JSON.parseObject(respJson, RpcfxResponse.class);
            /*
  request json: {"className":"com.hef.rpcfx.api.UserService","method":"findUser","params":[1]}
response json: {"result":"{\"@type\":\"com.hef.rpcfx.api.User\",\"id\":1,\"name\":\"Frankeleyn\"}","status":true,"exception":null}
find user id=1 from server: Frankeleyn
             */
        }
    }
}
