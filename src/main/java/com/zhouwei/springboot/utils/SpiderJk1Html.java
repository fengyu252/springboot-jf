package com.zhouwei.springboot.utils;

import com.zhouwei.springboot.entity.SjStory;
import com.zhouwei.springboot.entity.TangShi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *author:  zhouwei
 *time:  2020/4/8
 *function: 驾考爬虫
 */
public class SpiderJk1Html {

    public static void main(String[] args) {
        String url="https://tiba.jsyks.com/kmytk_1501";
            //url="https://tiba.jsyks.com/Post/9cab6.htm"; //有图
            //url="https://tiba.jsyks.com/Post/e3cab.htm";//无图
        findJkTk(url);

    }
    /**
     *author:  zhouwei
     *time:  2020/4/8
     *function:
     */
    public static  List<SjStory> findJkTkDetail(String url){
        List<SjStory> list=new ArrayList<SjStory>();
        List<Map<String,Object>> lt=findJkTk(url);
        SjStory ts=null;
        for(int i=0,len=lt.size();i<len;i++) {
            ts=new SjStory();
            Map<String,Object> mp=lt.get(i);
            String durl=mp.get("href").toString();
            String content=mp.get("content").toString();
            String result=mp.get("result").toString();
            String num=mp.get("num").toString();
            Document doc = getDocument(durl);
            Element element = doc.body();
            Elements elements = element.getElementsByAttributeValue("id", "question");
            //首先获取图片信息
            String imgUrl = elements.get(0).getElementsByTag("img").attr("src");
            Elements elements1 = element.getElementsByAttributeValue("id", "answer");
            String detail = elements1.get(0).getElementsByTag("h2").html();
            if (!imgUrl.isEmpty()) {
                int radnum=0;
                for(int j=0;;j++) {
                    radnum = (int) (Math.random() * 1000);
                    if(radnum>100){
                        break;
                    }
                }
                LocalDateTime localDateTime=LocalDateTime.now();
                //SimpleDateFormat sf=new SimpleDateFormat("yy");
                DateTimeFormatter sf=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String pathName=sf.format(localDateTime)+""+radnum;
                ts.setR1(imgUrl);
                ts.setType(1000);
                //ImgUrlUtil.downImgByUrl(imgUrl,pathName);
                ts.setImgUrl(pathName);
            } else {
                ts.setType(-1);
            }
            ts.setNames("客货车专用试题");
            ts.setContents(content);
            ts.setRes(result);
            ts.setXq(detail);
            ts.setLtype(4);
            ts.setR3(num);
            list.add(ts);
            //System.out.println(durl);
//            System.out.println(imgUrl);
//            System.out.println(detail);
        }
        return list;
    }
    /**
     *author:  zhouwei
     *time:  2020/4/8
     *function: 获取主题库信息 https://tiba.jsyks.com/kmytk_1501
     */
    public static List<Map<String,Object>> findJkTk(String url){
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Document doc=getDocument(url);
        Element element=doc.body();
        Elements elements=element.getElementsByAttributeValue("class","ListCnt");
        Elements elements1= elements.get(0).getElementsByTag("li");
        Map<String,Object> mp=null;
        for(int i=0,len=elements1.size();i<len;i++){
            mp=new HashMap<String,Object>();
            String content="";
            String href="";
            String da="";
            href=  elements1.get(i).getElementsByTag("a").attr("href");
            if(href.indexOf("https://tiba.jsyks.com")==-1){
                href="https://tiba.jsyks.com"+href;
            }
            String num=elements1.get(i).getElementsByTag("i").text().replace(".","");
            content=elements1.get(i).getElementsByTag("a").html();
            da= content.substring(content.lastIndexOf("<br>")+4);
            content=content.substring(0,content.lastIndexOf("<br>"));
            mp.put("content",content);
            mp.put("href",href);
            mp.put("result",da);
            mp.put("num",num);
            list.add(mp);
            //System.out.println( num + " = "+href +" = "+content +" = "+da);
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
