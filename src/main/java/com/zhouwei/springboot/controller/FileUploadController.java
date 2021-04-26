package com.zhouwei.springboot.controller;

import com.zhouwei.springboot.utils.ReadExlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/fileUpload")
public class FileUploadController {

    Logger logger= LoggerFactory.getLogger(getClass());

    /**
     * 单个文件上传
     */
    @PostMapping(value = "/exlUpload")
    @ResponseBody
    //@RequestParam("file")MultipartFile file
    public String uploadExcel(HttpServletRequest request) throws Exception {
        logger.info("FileUploadController- uploadExcel");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        MultipartFile file = multipartRequest.getFile("file");
        logger.info("fileName="+file.getOriginalFilename());
        if (file.isEmpty()) {
            return "文件不能为空";
        }
        InputStream inputStream = file.getInputStream();
        ReadExlUtil.readExl(inputStream,file.getOriginalFilename());
        inputStream.close();


        return "上传成功";
    }

}
