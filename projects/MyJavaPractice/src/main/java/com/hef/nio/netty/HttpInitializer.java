package com.hef.nio.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class HttpInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;

    public HttpInitializer(SslContext sslContext){
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslContext!=null){
            pipeline.addLast(sslContext.newHandler(ch.alloc()));
        }
        // 添加一个http的编码器，这样程序就知道要处理http报文
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1024*1024));
        pipeline.addLast(new HttpHandler());
    }
}
