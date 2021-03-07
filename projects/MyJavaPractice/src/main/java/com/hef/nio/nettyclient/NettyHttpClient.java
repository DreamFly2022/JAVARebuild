package com.hef.nio.nettyclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class NettyHttpClient {

    private final String host;
    private final int port;

    public NettyHttpClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;

        NettyHttpClient nettyHttpClient = new NettyHttpClient(host, port);
        nettyHttpClient.connect(host, port);
    }

    public void connect(String host, int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //  客户端接收的是httpResponse， 所以要使用HttpResponseDecoder 解码
                            pipeline.addLast(new HttpResponseDecoder());
                            // 客户端发送的是httpRequest， 所以要使用httpRequestEncoder进行编码
                            pipeline.addLast(new HttpResponseEncoder());
                            pipeline.addLast(new HttpClientOutboundHandler());
                        }
                    });

            ChannelFuture channelFuture = b.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
