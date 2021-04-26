package com.zhouwei.springboot.controller;

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
/**
 *author:  zhouwei
 *time:  2020/5/29
 *function: 文件上传下载
 *  1：文件下载
 *  2：单个文件上传
 *  3：多个文件上传
 */
@RequestMapping("/fileOption")
public class FileUpOrDownController {


    /**
     *
     * <p>单文件上传</p>
     *     <form method="post" action="/fileOption/upload" enctype="multipart/form-data">
     *       <input type="file" name="file"><br>
     *       <input type="submit" value="提交">
     *     </form>
     *     <p>多文件上传</p>
     *     <form method="post" action="/fileOption/bathUpload" enctype="multipart/form-data">
     *       <input type="file" name="file"><br>
     *       <input type="file" name="file"><br>
     *       <input type="file" name="file"><br>
     *       <input type="submit" value="提交">
     *     </form>
     *
     *
     */

    /**
     *author:  zhouwei
     *time:  2020/5/29
     *function: 文件下载
     */
    @RequestMapping("/downLuaFile")
    @ResponseBody
    public String downLuaFile(HttpServletRequest request,
                            HttpServletResponse response){
        String path="E://12.xlsx";
        String fileType=path.substring(path.lastIndexOf("."));
        String fileName="小伟";
        BufferedInputStream bis = null;
        OutputStream os = null;
        try{
            File file=new File(path);
            if(file.exists()){
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition",
                        "attachment;filename="
                                + java.net.URLEncoder.encode(fileName, "UTF-8")+
                                fileType);
                byte[] buff = new byte[1024];

                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }else{
                return "文件没找到";
            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(bis!=null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "ok";

    }


    /**
     *author:  zhouwei
     *time:  2020/5/29
     *function: 单个文件上传
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file){
        try{
            if(!file.isEmpty()){
                String fileName=file.getOriginalFilename();
                //后缀
                String suffxNmae=fileName.substring(fileName.lastIndexOf("."));
                String path="E:\\Lua"+fileName;

                File f=new  File(path);
                if(!f.exists()){
                    f.getParentFile().mkdirs();
                }
                file.transferTo(f);
            }else{
                return "file is null";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "异常";
        }
        return "ok";
    }
    /**
     *author:  zhouwei
     *time:  2020/5/29
     *function: 多个文件上传
     */
    @PostMapping("/bathUpload")
    @ResponseBody
    public String uploadLuaFile(HttpServletRequest request){
        List<MultipartFile> files=((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file=null;
        String path="E:\\Lua\\";
        try{
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                if (file.isEmpty()) {
                    return "上传第" + (i++) + "个文件失败";
                }
                String fileName = file.getOriginalFilename();

                File dest = new File(path + fileName);
                try {
                    file.transferTo(dest);
                    //LOGGER.info("第" + (i + 1) + "个文件上传成功");
                } catch (IOException e) {
                    //LOGGER.error(e.toString(), e);
                    return "上传第" + (i++) + "个文件失败";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "ok";

    }
}
