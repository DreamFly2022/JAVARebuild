package com.hef.nio.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class HttpClient01 {

    private static final Logger LOGGER = Logger.getLogger("com.hef.nio.httpclient.HttpClient01");

    /**
     * httpClient
     * 参考： https://blog.csdn.net/justry_deng/article/details/81042379
     * @param args
     */
    public static void main(String[] args) {
        // 获得Http客户端
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:8804/test");
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            // 从响应模型中获得实体
            HttpEntity entity = response.getEntity();
            LOGGER.info("响应状态为： " + response.getStatusLine());
            if (entity!=null){
                LOGGER.info("响应内容的长度: " + entity.getContentLength());
                System.out.println(EntityUtils.toString(entity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }

}
