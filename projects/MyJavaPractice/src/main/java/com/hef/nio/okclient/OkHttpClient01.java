package com.hef.nio.okclient;

import okhttp3.*;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class OkHttpClient01 {

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8804/test")
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            InputStream inputStream = body.byteStream();
            System.out.println("响应的长度： "+ body.contentLength());
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
