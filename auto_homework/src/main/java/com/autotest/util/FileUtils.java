package com.autotest.util;

import java.io.*;

public class FileUtils {
    public static void copyDir(File source, File dest) {
        if (!dest.exists()) {
            dest.mkdir();
        }
        File[] files = source.listFiles();
        if (files != null) {
            for(File file : files) {
                if (file.isFile()) {
                    // 直接复制
                    copyFile(file, new File(dest, file.getName()));
                } else {
                    copyDir(file, new File(dest, file.getName()));
                }
            }
        }
    }

    private static void copyFile(File source, File dest) {
        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest))) {
            byte[] bytes = new byte[8192];
            int len;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTarget(File source) {
        File[] files = source.listFiles();
        if (files != null) {
            for (File file : files) {
                //如果是文件夹，而且名字是target 删除
                if(file.isDirectory() && file.getName().equals("target")) {
                    //删除文件
//                    file.delete();
                    deleteFile(file);
                //如果是文件夹，名字不是target就向下
                } else if(file.isDirectory()) {
                    deleteTarget(file);
                }
            }
        }
    }

    /**
     * 后序遍历删除
     * @param deleteFile
     */
    private static void deleteFile(File deleteFile) {
        File[] files = deleteFile.listFiles();
        if(files != null) {
            for (File file : files) {
                if(file.isDirectory()){
                    deleteFile(file);
                } else if(file.isFile()) {
                    file.delete();
                }
            }
        }
        deleteFile.delete();
    }
}
