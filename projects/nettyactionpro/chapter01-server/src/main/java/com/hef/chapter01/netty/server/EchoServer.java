package com.hef.chapter01.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty服务的引导
 *@Date 2022/3/1
 * @Author lifei
 */
public class EchoServer {

    private final int port;
    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        // 自定义的ChannelHandler
        final EchoServerHandler echoServerHandler = new EchoServerHandler();

        // 用户接收新的客户端连接
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 具体处理任务
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            // 引导
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        }finally {
            parentGroup.shutdownGracefully().sync();
            childGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) {
        try {
            new EchoServer(8988).start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
