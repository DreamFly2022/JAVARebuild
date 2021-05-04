package com.hef.nio.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author lifei
 * @since 2021/3/7
 */
public interface HttpRequestFilter {


    // load balance : random  随机、 Weight 权重、RoundRibbon 挨个调用
    void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx);
}
