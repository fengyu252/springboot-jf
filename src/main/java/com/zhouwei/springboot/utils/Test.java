package com.zhouwei.springboot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {

    public static void main(String[] args) {
        try {
//            String s = ResourceUtils.getURL("classpath:").getPath();
//            System.out.println(s);
            String url="http://www.banzhuren.cn/jiaoan/7335.html";
            spiderHtml(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void spiderHtml(String url) {

        Document doc = getDocument(url);
        Element element = doc.body();
        System.out.println(doc.text());
    }

    public static Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


