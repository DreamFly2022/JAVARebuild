package com.hef.rpcfx.api;

/**
 * 查找具体的实现
 */
public interface RpcfxResolver {

    /**
     * 根据类型获取实现
     * @param serviceClass
     * @return
     */
    Object resolver(String serviceClass);
}
