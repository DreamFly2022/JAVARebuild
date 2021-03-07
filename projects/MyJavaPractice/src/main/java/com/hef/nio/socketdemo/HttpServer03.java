package com.hef.nio.socketdemo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lifei
 * @since 2021/3/4
 */
public class HttpServer03 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(37);
        try {
            ServerSocket serverSocket = new ServerSocket(8803);
            while (true){
                Socket socket = serverSocket.accept();
                executorService.execute(()->{service(socket);});
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void service(Socket socket){
        try (OutputStream outputStream = socket.getOutputStream();
             PrintWriter printWriter = new PrintWriter(outputStream, true)){
            String contentStr="hello,nio\r\n";
            Thread.sleep(20);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Accept-Ranges: bytes");
            printWriter.println("Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform");
            printWriter.println("Connection: keep-alive");
            printWriter.println("Content-length:"+contentStr.length());
            printWriter.println("Content-Type: text/html;charset=utf-8");
            printWriter.println();
            printWriter.write(contentStr);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
