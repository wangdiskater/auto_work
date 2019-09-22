package com.autotest;

import com.autotest.util.FileUtils;
import com.autotest.util.HttpClientUtils;
import com.autotest.util.ZipUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class StartUpload {
    public static void main(String[] args) throws ParseException {

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
                //第一步先找到我们的源文件。
                String sourcePath = "F:/ideaObject/Spring/_0920";
                //需要把文件名拿出来,之后拼接名字
                String[] split = sourcePath.split("/");
                String fileName = split[split.length-1];

                //第二步递归全部复制出来：
                String destPath ="H:\\test\\" + fileName + "_王邸";
                File source = new File(sourcePath);
                File dest = new File(destPath);
                FileUtils.copyDir(source, dest);

                //第三步递归删除target
                FileUtils.deleteTarget(dest);

                //第四步打包文件
                String srcPath = destPath;
                String zipName = srcPath+".zip";
                //文件输出流名字
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File(zipName));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ZipUtils.toZip(srcPath, fileOutputStream,true);

                //第五步上传到服务器
                String url = "http://localhost:12345/file";
                HttpClientUtils.upload(zipName,url);

            }
        },uploadTime);
    }
}
