package com.zhouwei.springboot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public   class  HttpGetOrPostUtil {

    /**
     * http://ip.taobao.com/service/getIpInfo.php?ip=111.178.151.11
     * 淘宝接口查询ip地址信息
     * @param requestUrl
     * @return
     */

    public static JSONObject sendGetUrl(String requestUrl) {
        String res = "";
        JSONObject object = null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            // 提交模式
            urlCon.setRequestMethod("GET");
            //连接超时 单位毫秒
            urlCon.setConnectTimeout(10000);
            //读取超时 单位毫秒
            urlCon.setReadTimeout(2000);
            urlCon.setDoOutput(true);
//            urlCon.setRequestProperty("Accept", "*/*");
//            urlCon.setRequestProperty("Accept-Charset", "utf-8");
//            urlCon.setRequestProperty("Accept-Encoding", "gzip, deflate");
//            urlCon.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
//            urlCon.setRequestProperty("Cache-Control", "max-age=0");
//            urlCon.setRequestProperty("Connection", "keep-alive");
//            urlCon.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
//            urlCon.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
            urlCon.connect();
            if (200 == urlCon.getResponseCode()) {
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                object= JSON.parseObject(res);

            } else {
                throw new Exception("连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject sendPostUrl(String path, String param) {
        JSONObject object = null;
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 提交模式
            httpURLConnection.setRequestMethod("POST");
            //连接超时 单位毫秒
            httpURLConnection.setConnectTimeout(10000);
            //读取超时 单位毫秒
            httpURLConnection.setReadTimeout(2000);
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            //post的参数 xx=xx&yy=yy
            printWriter.write(param);
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            object= JSON.parseObject(bos.toString("utf-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /****
     * 发送固定连接信息
     * @param
     * @return
     */
    public static JSONObject sendGetUrl2(String bundleId,String r1,String ip,String idfa,String r4,String model,String oss,Integer taskType) {

        String res = "";
        JSONObject object = null;
        StringBuffer buffer = new StringBuffer();
        try {
            String requestUrl="";
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.SECOND,-190);
            long launch=calendar.getTimeInMillis();
            calendar.add(Calendar.SECOND,7);
            long click=calendar.getTimeInMillis();
            calendar.add(Calendar.SECOND,183);
            long close=calendar.getTimeInMillis();
            if(taskType==0) {
                requestUrl = "http://cpa.asoplus.cn:51168/report?" +
                        "bundleid=" + bundleId + "&keyword=" + r1 + "&ip=" + ip
                        + "&idfa=" + idfa + "&udid=" + r4
                        + "&model=" + model + "&os=" + oss
                        + "&launch=" + launch + "&click=" + click + "&close=" + close + "&cdate=&taskid=" + r4
                        + "&vendor=jfq&source=cd";

            }else{
                requestUrl = "http://cpa.asoplus.cn:51168/report?" +
                        "bundleid=" + bundleId + "&keyword=" + r1 + "&ip=" + ip
                        + "&idfa=" + idfa + "&udid=" + r4
                        + "&model=" + model + "&os=" + oss
                        + "&launch=" + launch + "&click=" + click + "&close=" + close + "&cdate="+System.currentTimeMillis()+"&taskid=" + r4
                        + "&vendor=jfq&source=cd";

            }

            URL url = new URL(requestUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            // 提交模式
            urlCon.setRequestMethod("GET");
            //连接超时 单位毫秒
            urlCon.setConnectTimeout(10000);
            //读取超时 单位毫秒
            urlCon.setReadTimeout(2000);
            urlCon.setDoOutput(true);

            urlCon.connect();
            if (200 == urlCon.getResponseCode()) {
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                object= JSON.parseObject(res);

            } else {
                System.out.println("连接失败");
            }
        } catch (Exception e) {

        }
        return object;
    }
}
