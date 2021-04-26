package com.zhouwei.springboot.utils;

import com.zhouwei.springboot.entity.SjStory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *author:  zhouwei
 *time:  2020/4/3
 *function: 爬取笑话数据
 */
public class SpiderXhHtml {

    public static void main(String[] args) {
        String
        url="https://www.xiaohuayoumo.com/lengxiaohua/";
        url="https://www.xiaohuayoumo.com/lengxiaohua/21833.html";
        Document doc=getDocument(url);
        Element element=doc.body();
        System.out.println(doc.html());
        Elements elements= element.getElementsByAttributeValue("class","post-content");



    }

    public static  List<SjStory> findXhMess(String url){
        List<SjStory> list=new ArrayList<SjStory>();
        List<Map<String,Object>> lt=findXh(url);
        SjStory sjStory=null;
        for(int i=0,len=lt.size();i<len;i++){
            Map<String,Object> mp=lt.get(i);
            sjStory=new SjStory();
            Document doc=getDocument(mp.get("href").toString());
            Element element=doc.body();
            System.out.println(doc.html());
            Elements elements= element.getElementsByAttributeValue("class","post-content");
            sjStory.setNames(mp.get("name").toString());
            sjStory.setContents(elements.html());
            sjStory.setType(101);
            sjStory.setR1("冷笑话");
            list.add(sjStory);
        }
        return list;
    }

    /**
     * 爬取笑话分页数据
     * @param url
     * @return
     */
    public static List<Map<String,Object>> findXh(String url){
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Document doc=getDocument(url);
        Element element=doc.body();
        //System.out.println(doc.html());
        Elements elements= element.getElementsByAttributeValue("class","post-header ");
        Map<String,Object> mp=null;
       // System.out.println(elements.size());
        for(int i=0,len=elements.size();i<len;i++){
            mp=new HashMap<String,Object>();
            Elements elements1=elements.get(i).getElementsByTag("h2");
            String text=elements1.text();
            String href=elements1.get(0).getElementsByTag("a").attr("href");
            //System.out.println(elements1.attr("href"));
            //System.out.println(href+"===="+text);
            if(href.indexOf("https://www.xiaohuayoumo.com")==-1){
                href="https://www.xiaohuayoumo.com"+href;
            }
            mp.put("href",href);
            mp.put("name",text);
            list.add(mp);
        }
        return list;
    }


    public static Document getDocument (String url){
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
