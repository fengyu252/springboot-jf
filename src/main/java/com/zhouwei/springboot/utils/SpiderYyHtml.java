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
 *function: 爬去故事信息 http://www.xigushi.com/yygs/
 */
public class SpiderYyHtml {

    public static void main(String[] args) {
        String url ="http://www.xigushi.com/yygs/";
        url="http://www.xigushi.com/yygs/13913.html";
//        Document doc=getDocument(url);
//        Element element=doc.body();
//        Elements elements= element.getElementsByAttributeValue("class","by");
//        Elements elements1=elements.get(0).getElementsByTag("dd");
//        Elements elements2=elements1.get(0).getElementsByTag("p");
//        System.out.println(elements2.html());

    }


    /**
     * 爬去具体故事信息
     * @param url
     * @return
     */
    public static List<SjStory> findGushiMess(String url){
        List<SjStory> list=new ArrayList<SjStory>();
        SjStory sjStory=null;
        List<Map<String,Object>> lt= fingGushiPage(url);
        for(int i=0,len=lt.size();i<len;i++) {
            sjStory=new SjStory();
            Map<String,Object> mp=lt.get(i);
            String urlm=mp.get("herf").toString();
            Document doc = getDocument(urlm);
            Element element = doc.body();
            Elements elements = element.getElementsByAttributeValue("class", "by");
            Elements elements1 = elements.get(0).getElementsByTag("dd");
            Elements elements2 = elements1.get(0).getElementsByTag("p");
            System.out.println(elements2.html());
            sjStory.setNames(mp.get("name").toString());
            sjStory.setContents(elements2.html());
            sjStory.setType(11);
            sjStory.setR1("幽默故事");
            list.add(sjStory);
        }
        return list;
    }

    /**
     * 查询分页主页信息
     * 爬出href 和标题信息
     * @param url
     * @return
     */
    public static List<Map<String,Object>> fingGushiPage(String url){
        List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
        Map map=null;
        Document doc=getDocument(url);
        Element element=doc.body();
        Elements elements=element.getElementsByAttributeValue("class","list");
        Elements elements1=  elements.get(0).getElementsByTag("dd");
        Elements elements2=elements1.get(0).getElementsByTag("li");
       // System.out.println(elements2.size());
        for(int i=2,len=elements2.size();i<len;i++){
            map=new HashMap();
            String href= elements2.get(i).getElementsByTag("a").attr("href");
            String text=elements2.get(i).getElementsByTag("a").text();
            if(href.indexOf("http://www.xigushi.com")==-1){
                href="http://www.xigushi.com"+href;
            }
            map.put("herf",href);
            map.put("name",text);
            list.add(map);
           // System.out.println(href +" === "+text);
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
