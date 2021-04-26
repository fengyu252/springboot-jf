package com.zhouwei.springboot.utils;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImgUrlUtil {


//    public static void main(String[] args) {
//        downImgByUrl("https://sucimg.itc.cn/sblog/jd5YYEgpCh6","2020");
//    }

    /**
     *author:  zhouwei
     *time:  2020/4/8
     *function: 下载图片信息 通过url 连接
     */
    public static  boolean downImgByUrl(String url,String name){
        try{
            URL httpUrl=new URL(url);
            String path="G:\\jkimg\\kmy2\\"+name+".jpg";
            //存储目录再G:\jkimg\jkyt
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 得到输入流
            InputStream inputStream = conn.getInputStream();
            // 获取自己数组
            byte[] getData = readInputStream(inputStream);
            // 文件保存位置
//            File saveDir = new File(path);
//            if (!saveDir.exists()) {
//                saveDir.mkdirs();
//                //logger.info("创建文件夹:" + saveDir.exists());
//            }
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }

            System.out.println("ok");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void dowImg2(String url,String name){
        try {
            String path="G:\\jkimg\\kmy1\\"+name+".jpg";
            URL httpUrl = new URL(url);
            DataInputStream dataInputStream = new DataInputStream(httpUrl.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(buffer);//返回Base64编码过的字节数组字符串
            System.out.println(encode);
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    private static List<File> fileList = new ArrayList<File>();

    public static void main(String[] args) {

    //图片所在的根目录 , 图片去除水印后的存储目录
      convertAllImages("G:\\jkimg\\sy\\20200408140918386.jpg", "G:\\jkimg\\sy\\xg\\20200408140918386.jpg"); //支持批量去除图片水印
        //convertAllImages("G:\\jkimg\\sy\\20200408140919296.jpg", "G:\\jkimg\\sy\\xg\\20200408140919296.jpg"); //支持批量去除图片水印

    }

    private static void convertAllImages(String dir, String saveDir) {
        File dirFile = new File(dir);
        File saveDirFile = new File(saveDir);
        dir = dirFile.getAbsolutePath();
        saveDir = saveDirFile.getAbsolutePath();
        loadImages(new File(dir));
        for (File file : fileList) {
            String filePath = file.getAbsolutePath();

            String dstPath = saveDir + filePath.substring(filePath.indexOf(dir) + dir.length(), filePath.length());
            System.out.println("converting: " + filePath);
            replaceColor(file.getAbsolutePath(), dstPath);
        }
    }

    public static void loadImages(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (int i = 0; i < fileArray.length; i++) {
                        //递归调用
                        loadImages(fileArray[i]);
                    }
                }
            } else {
                String name = f.getName();
                if (name.endsWith("png") || name.endsWith("jpg")) {
                    fileList.add(f);
                }
            }
        }
    }

    private static void replaceFolderImages(String dir) {
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles(new FileFilter() {
            public boolean accept(File file) {
                String name = file.getName();
                if (name.endsWith("png") || name.endsWith("jpg")) {
                    return true;
                }
                return false;
            }
        });
        for (File img : files) {
            replaceColor(img.getAbsolutePath(), img.getAbsolutePath());
        }
    }

    private static void replaceColor(String srcFile, String dstFile) {
        try {
            Color color = new Color(255, 195, 195);
            replaceImageColor(srcFile, dstFile, color, Color.WHITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replaceImageColor(String file, String dstFile, Color srcColor, Color targetColor) throws IOException {
        URL http;
        if (file.trim().startsWith("https")) {
            http = new URL(file);
            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
            conn.setRequestMethod("GET");
        } else if (file.trim().startsWith("http")) {
            http = new URL(file);
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
            conn.setRequestMethod("GET");
        } else {
            http = new File(file).toURI().toURL();
        }
        BufferedImage bi = ImageIO.read(http.openStream());
        if(bi == null){
            return;
        }

//Color wColor = new Color(255, 255, 255);//白色
        Color wColor = new Color(238, 243, 249);//浅灰色
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                //System.out.println(bi.getRGB(i, j));
                int color = bi.getRGB(i, j);
                Color oriColor = new Color(color);
                int red = oriColor.getRed();
                int greed = oriColor.getGreen();
                int blue = oriColor.getBlue();
                //粉色
                if (greed < 190 || blue < 190) {

                } else {
                //去掉粉色水印(粉色替换为白色)
                //	if (red == 255 && greed > 180 && blue > 180) {
                //	bi.setRGB(i, j, wColor.getRGB());
                //	}
                //去掉灰色水印（灰色替换为白色）
                //	if (red == 229 && greed == 229 && blue == 229) {
                //	bi.setRGB(i, j, wColor.getRGB());
                //	}
                //去掉浅灰色水印（灰色替换为白色或替换为浅灰色）
//                    if (red >170 && greed > 170 && blue > 170) {
//                        bi.setRGB(i, j, wColor.getRGB());
//                    }
                    if(red ==255&&greed==255&&blue==255){
                        bi.setRGB(i, j, wColor.getRGB());
                    }
                }
            }
        }
        String type = file.substring(file.lastIndexOf(".") + 1, file.length());
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(type);
        ImageWriter writer = it.next();
        File f = new File(dstFile);
        f.getParentFile().mkdirs();
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        bi.flush();
        ios.flush();
        ios.close();
    }

}
