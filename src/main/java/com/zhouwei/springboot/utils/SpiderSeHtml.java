package com.zhouwei.springboot.utils;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import com.zhouwei.springboot.entity.SjStory;
import com.zhouwei.springboot.entity.TangShi;
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
 *function: 爬去故事信息 http://www.gushi365.com/youergushi/index.html
 */
public class SpiderSeHtml {

    public static void main(String[] args) {
        String url="http://www.gushi365.com/youergushi/index.html";
                url="http://www.gushi365.com/info/13562.html";
        Document doc=getDocument(url);
        Element element=doc.body();
        Elements ss=element.getElementsByAttributeValue("class","single-content");
        //Elements  s=elements.get(0).getAllElements();
        //Element et=Jsoup.parse(s);
        //s.removeAttr("src");
        ss.get(0).getElementsByTag("div").remove();
        ss.get(0).getElementsByAttributeValue("class","STYLE1").remove();
        ss.get(0).getElementsByTag("a").remove();
        ss.get(0).getElementsByTag("h2").remove();
        ss.get(0).getElementsByTag("img").remove();
       String content=ss.get(0).html().replace("｜","").replace("<p></p>","");
        System.out.println(content.trim());
    }



    /**
     * @param url
     */
    public static List findYouer(String url){
        Document doc=getDocument(url);
        Element element=doc.body();
        Elements elements= element.getElementsByAttributeValue("class","entry-header");
        Map<String,Object> mp=null;
        List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<elements.size();i++){
            mp=new HashMap<String,Object>();
            Elements element1= elements.get(i).getElementsByTag("a");
           // System.out.println(element1.text()+"==" + element1.get(0).attr("href"));
            mp.put("content",element1.text());
            String href=element1.get(0).attr("href");
            if(href.indexOf("https")!=-1) {
                mp.put("href", href );
            }else{
                mp.put("href", "http://www.gushi365.com" +href );
            }
            list.add(mp);
        }
        return list;
    }
    //
    public static List<SjStory> findGuShi(String url){
        List<SjStory> lt=new ArrayList<SjStory>();
        List<Map<String,Object>> list=  findYouer(url);
        SjStory ts=null;
        if(list!=null&&list.size()>0){
            for(int i=0,len= list.size();i<len;i++){
                ts=new SjStory();
                Map<String,Object> mp=list.get(i);
                Document doc=getDocument(mp.get("href").toString());
                Element element=doc.body();
                Elements ss=element.getElementsByAttributeValue("class","single-content");
                ss.get(0).getElementsByTag("div").remove();
                ss.get(0).getElementsByAttributeValue("class","STYLE1").remove();
                ss.get(0).getElementsByTag("a").remove();
                ss.get(0).getElementsByTag("h2").remove();
                ss.get(0).getElementsByTag("img").remove();
                String content=ss.get(0).html().replace("｜","").replace("<p></p>","");
                //System.out.println(content.trim());
               // String s=elements.text();
               // String ss= s.substring(0,s.indexOf("｜"));
                ts.setNames(mp.get("content").toString());
                ts.setContents(content.trim());
                ts.setType(10);
                ts.setR1("辣爸辣妈");
                lt.add(ts);
            }
            System.out.println("结束: "+ url);
        }else{
            System.out.println("没得连接结束");
        }
        return lt;
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
