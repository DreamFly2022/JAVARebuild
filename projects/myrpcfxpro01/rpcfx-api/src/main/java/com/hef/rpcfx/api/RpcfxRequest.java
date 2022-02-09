package com.hef.rpcfx.api;

/**
 * rpc 请求
 * @Date 2022/2/9
 * @Author lifei
 */
public class RpcfxRequest {

    private String className;
    private String method;
    private Object[] params;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
}
