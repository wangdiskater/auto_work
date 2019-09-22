package com.autotest;

import com.autotest.util.FileUtils;
import com.autotest.util.HttpClientUtils;
import com.autotest.util.ZipUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyTest {
    /***
     * 简单测试
     */
    @Test
    public void test1(){

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:12345/doGetControllerOne");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    /**
     * 文件上传
     *
     */
    @Test
    public void PostTest(){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost("http://localhost:12345/file");
        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

            multipartEntityBuilder.setCharset(Charset.forName("utf-8"));
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

            // 第一个文件
            String filesKey = "files";
            File file1 = new File("H:\\test\\头像.jpg");
            multipartEntityBuilder.addBinaryBody(filesKey,file1);



            // 第二个文件(多个文件的话，使用可一个key就行，后端用数组或集合进行接收即可)
//            File file2 = new File("H:\\test\\头像.jpg");

            // 防止服务端收到的文件名乱码。 我们这里可以先将文件名URLEncode，然后服务端拿到文件名时在URLDecode。就能避免乱码问题。
            // 文件名其实是放在请求头的Content-Disposition里面进行传输的，如其值为form-data; name="files"; filename="头像.jpg"
//            multipartEntityBuilder.addBinaryBody(filesKey, file2, ContentType.DEFAULT_BINARY, URLEncoder.encode(file2.getName(), "utf-8"));
//            multipartEntityBuilder.addBinaryBody(filesKey, ContentType.DEFAULT_BINARY, URLEncoder.encode(file2.getName(), "utf-8"));

            // 其它参数(注:自定义contentType，设置UTF-8是为了防止服务端拿到的参数出现乱码)
//            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
//            multipartEntityBuilder.addTextBody("name", "王邸", contentType);
//            multipartEntityBuilder.addTextBody("age", "25", contentType);

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

    /**
     * 测试文件压缩
     */
    @Test
    public void zipTest() throws FileNotFoundException {

        String srcPath = "H:\\test\\王邸";
        String zipName = srcPath+".zip";
        //文件输出流名字
        FileOutputStream fileOutputStream = new FileOutputStream(new File(zipName));

        ZipUtils.toZip(srcPath, fileOutputStream,true);

    }

    /**
     *
     * 整个过程合起来
     */
    @Test
    public void sendWork() throws FileNotFoundException {


        //第一步先找到我们的源文件。
        String sourcePath = "F:/ideaObject/Spring/_0920";
        //需要把文件名拿出来,之后拼接名字
        String[] split = sourcePath.split("/");
        String fileName = split[split.length-1];

        //第二步递归全部复制出来：
        String destPath ="H:\\test\\" + fileName + "_完了";
        File source = new File(sourcePath);
        File dest = new File(destPath);
        FileUtils.copyDir(source, dest);

        //第三步递归删除target
        FileUtils.deleteTarget(dest);

        //第四步打包文件
        String srcPath = destPath;
        String zipName = srcPath+".zip";
        //文件输出流名字
        FileOutputStream fileOutputStream = new FileOutputStream(new File(zipName));
        ZipUtils.toZip(srcPath, fileOutputStream,true);

        //第五步上传到服务器
        HttpClientUtils.upload(zipName);
    }

    /**
     * 加上定时任务和文件名的配置
     *
     */
    @Test
    public void sendWorkEnd() throws java.text.ParseException {
        Timer timer = new Timer();
//        void schedule(TimerTask task, Date time) 在指定的时间执行指定的任务(一次)
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        String[] s = format.split(" ");
        String upload = s[0] + " 22:30:00";
        Date uploadTime = sdf.parse(upload);


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("拉闸了");
            }
        },uploadTime);

    }


}
