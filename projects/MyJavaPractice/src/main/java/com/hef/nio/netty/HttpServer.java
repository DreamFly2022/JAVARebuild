package com.hef.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.net.ServerSocket;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

/**
 * @author lifei
 * @since 2021/3/4
 */
public class HttpServer {

    private static final Logger LOGGER = Logger.getLogger("com.hef.nio.netty.HttpServer");

    private final boolean ssl;
    private final int port;

    public HttpServer(boolean ssl, int port){
        this.ssl = ssl;
        this.port = port;
    }


    public void run() throws CertificateException, SSLException, InterruptedException {
        // Https
        SslContext sslContext = null;
        if (ssl){
            SelfSignedCertificate ssc =  new SelfSignedCertificate();
            sslContext = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        }
        // 线程数可以调整
        EventLoopGroup bossGroup = new NioEventLoopGroup(3);
        EventLoopGroup workGroup = new NioEventLoopGroup(1000);
        try{

            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpInitializer(sslContext));
            Channel channel = b.bind(port).sync().channel();
            LOGGER.info("开启netty http服务，监听地址和端口为： " + (ssl? "Https":"http") + "://127.0.0.1:"+port + "/");
            channel.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
