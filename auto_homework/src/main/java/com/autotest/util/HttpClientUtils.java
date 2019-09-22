package com.autotest.util;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpClientUtils {

    public static void upload(String zipName) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost("http://localhost:12345/file");
        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

            //设置参数，防止文件名乱码
            multipartEntityBuilder.setCharset(Charset.forName("utf-8"));
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // 文件
            String filesKey = "files";
            File file1 = new File(zipName);
            multipartEntityBuilder.addBinaryBody(filesKey,file1);

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("HTTPS响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("HTTPS响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                System.out.println("HTTPS响应内容为:" + responseStr);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
