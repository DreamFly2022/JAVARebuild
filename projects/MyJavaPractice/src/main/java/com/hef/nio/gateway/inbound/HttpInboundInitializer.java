package com.hef.nio.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private final String proxyServer;

    public HttpInboundInitializer(String proxyServer){
        this.proxyServer = proxyServer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //添加处理器
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new HttpInboundHandler(proxyServer));

    }
}
