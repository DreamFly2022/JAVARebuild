package com.hef.demo.grpc.server;

import com.hef.demo.grpc.api.Feature;
import com.hef.demo.grpc.api.RouteGuideUtil;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 启动服务入口
 * @Date 2022/2/27
 * @Author lifei
 */
public class RouteGuideServer {

    private static final Logger logger = Logger.getLogger(RouteGuideServer.class.getName());

    private final int port;
    private final Server server;

    public RouteGuideServer(int port) {
        this(port, RouteGuideUtil.getDefaultFeaturesFile());
    }

    /**
     * 通过监听port端口，并使用featureFile数据库创建 RouteGuideServer
     * @param port
     * @param featureFile
     */
    public RouteGuideServer(int port, URL featureFile) {
        this(ServerBuilder.forPort(port), port, RouteGuideUtil.parseFeatures(featureFile));
    }

    /**
     * 使用serverBuilder作为基础，features作为数据去创建RouteGuide
     * @param serverBuilder
     * @param port
     * @param features
     */
    public RouteGuideServer(ServerBuilder<?> serverBuilder, int port, Collection<Feature> features) {
        this.port = port;
        this.server = serverBuilder.addService(new RouteGuideService(features)).build();
    }

    /**
     * 开始服务请求
     * @throws IOException
     */
    public void start() throws IOException {
        server.start();
        logger.info("服务启动， 监听的端口为：" + port);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.err.println("*** shutting down gRPC server since JVM is shutting down ***");
            try {
                RouteGuideServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    /**
     * 关闭服务并关闭资源
     * @throws InterruptedException
     */
    public void stop() throws InterruptedException {
        if (server!=null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * 因为grpc 库是一个守护线程，因此要等待主线程终止
     * @throws InterruptedException
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server!=null) {
            server.awaitTermination();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        RouteGuideServer server = new RouteGuideServer(8980);
        server.start();
        server.blockUntilShutdown();
    }
}
