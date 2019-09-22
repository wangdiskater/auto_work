package com.autotest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Controller
public class MyController {

    @RequestMapping("/doGetControllerOne")
    public String doGetControllerOne(){
        return "123";
    }
    @RequestMapping(value = "/file")
    public String fileControllerTest(MultipartFile files) throws UnsupportedEncodingException {
        String fileName = files.getOriginalFilename();
//        fileName = URLDecoder.decode(fileName,"utf-8");


        return "123";
    }

/*    @RequestMapping(value = "/file")
    public String fileControllerTest(@RequestParam("name") String name,
                                     @RequestParam("age") Integer age
//                                     @RequestParam("files")List<MultipartFile> multipartFiles
                                     ) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(64);
        sb.append("\n");
        sb.append("name=").append(name).append("\tage=").append(age);
        String filename;
        for (MultipartFile mutipartFile : multipartFiles) {
            sb.append(("\n文件信息：\n"));
            filename = mutipartFile.getOriginalFilename();
            if(filename == null){
                continue;
            }
            filename = URLDecoder.decode(filename,"utf-8");
            sb.append("\t文件名：").append(filename);
            sb.append("\t文件大小：").append(mutipartFile.getSize()*1.0/1024).append("KB");
        }
        return sb.toString();
    }*/





















}
