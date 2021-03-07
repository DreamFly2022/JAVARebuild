package com.hef.nio.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class HttpInboundServer {

    private final String proxyServer;
    private final int port;

    public HttpInboundServer(String proxyServer, int port){
        // 后面要改成rounter 的时候，这个地方要改成一个列表
        this.proxyServer = proxyServer;
        this.port = port;
    }

    public void run(){

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(16);
        try {
            ServerBootstrap b = new ServerBootstrap();
            // 一堆参数
            // 前面是网络，最后一个是netty相关
            b.option(ChannelOption.SO_BACKLOG, 128)   //  连理socket 连接时，未完成的连接可以保存的个数
                    .option(ChannelOption.TCP_NODELAY, true) // nagel 算法， 在高性能下改成false是比较友好的
                    .option(ChannelOption.SO_KEEPALIVE, true)  // 保持http是长连接
                    .option(ChannelOption.SO_REUSEADDR, true)  //  重用地址， 复用Time-wait状态的地址
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)  //  receive buf  缓冲区
                    .option(ChannelOption.SO_SNDBUF, 32*1024)    // send buf  缓冲区
                    .option(EpollChannelOption.SO_REUSEPORT, true)  // 重用端口， 复用Time-wait状态的端口
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  // boss 延伸下去的是work
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 定义一个buf的内存池， 反复使用这块内存，这样效率比较高。

            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpInboundInitializer(this.proxyServer));

            Channel channel = b.bind(port).sync().channel();
            System.out.println("开启netty服务器，监听地址和端口为： http://127.0.0.1:" +  port + "/");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

}
