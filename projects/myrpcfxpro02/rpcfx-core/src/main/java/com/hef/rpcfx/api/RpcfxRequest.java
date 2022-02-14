package com.hef.rpcfx.api;

import java.util.Arrays;

/**
 * 请求对象
 * @Date 2022/2/15
 * @Author lifei
 */
public class RpcfxRequest {

    private String serviceClass;
    private String method;
    private Object[] params;

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "RpcfxRequest{" +
                "serviceClass='" + serviceClass + '\'' +
                ", method='" + method + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
