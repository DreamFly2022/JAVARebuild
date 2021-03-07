package com.hef.nio.netty;

/**
 * @author lifei
 * @since 2021/3/4
 */
public class NettyServerApplication {

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(false, 8804);
        try {
            httpServer.run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
