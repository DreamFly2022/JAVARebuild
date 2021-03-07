package com.hef.nio.gateway;

import com.hef.nio.gateway.inbound.HttpInboundServer;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class NettyServerApplication02 {

    private static final String GATEWAY_NAME = "NIOGateway";
    private static final String GATEWAY_VERSION = "1.0.0";

    public static void main(String[] args) {
        String proxyServer = System.getProperty("proxyServer", "http://localhost:8080");
        String proxyPort = System.getProperty("proxyPort", "8888");

        // 网关的地址： http://localhost:8888/hello
        // 真实的地址： http://localhost:8080/hello

        int port = Integer.parseInt(proxyPort);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " start....");
        HttpInboundServer httpInboundServer = new HttpInboundServer(proxyServer, port);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " started at http://localhost:" + port + " for servers: " + proxyServer);
        try{
            httpInboundServer.run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
