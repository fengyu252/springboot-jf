package com.zhouwei.springboot.utils;

import ch.qos.logback.classic.net.SyslogAppender;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.zhouwei.springboot.entity.TangShi;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
/**
 *author:  zhouwei
 *time:  2020/4/3
 *function: 爬去古诗唐诗信息  https://so.gushiwen.org/gushi/xiaoxue.aspx
 */
public class SpiderHtml {

    private Logger logger= LoggerFactory.getLogger(SpiderHtml.class);

    public static void main(String[] args) {
        /**
         *  https://so.gushiwen.org/gushi/xiaoxue.aspx
         *  https://so.gushiwen.org/shiwenv_ef9cd9ba44bb.aspx
         */
//        String url="https://so.gushiwen.org/authors/default.aspx?p=1&c=";
//
//        Document document=getDocument(url);
//        Elements doc= document.getElementsByAttributeValue("class","sonspic");
//        //System.out.println(doc.get(0).html());
//        for(int i=0,l=doc.size();i<l;i++){
//            Elements ss= doc.get(i).getElementsByTag("p");
////            System.out.println(ss.get(0).text());
////            System.out.println(ss.get(1).text());
//            System.out.println(ss.get(0).text()+"=="+ss.get(1).text());
//        }
       // Elements ss= doc.get(0).getElementsByTag("p");
        Document doc=getDocument("https://so.gushiwen.org/search.aspx?value=%E7%8E%8B%E7%BB%A9");
        System.out.println(doc.html());
        //getTanshi("https://so.gushiwen.org/wenyan/xiaowen.aspx");
    }
    /**
     *author:  zhouwei
     *time:  2020/3/19
     *function: 获取古诗词作者信息
     */
    public static List getTanshiAuthor(){
        List list=new ArrayList();
        TangShi ts=null;
        for(int j=1;j<=100;j++) {
            String url = "https://so.gushiwen.org/authors/Default.aspx?p="+j+"&c=清代";

            Document document = getDocument(url);
            Elements doc = document.getElementsByAttributeValue("class", "sonspic");
            //System.out.println(doc.get(0).html());
            for (int i = 0, l = doc.size(); i < l; i++) {
                Elements ss = doc.get(i).getElementsByTag("p");
                ts=new TangShi();
                ts.setNj("清代");
                ts.setName(ss.get(0).text());
                ts.setContent(ss.get(1).text());
                list.add(ts);
//            System.out.println(ss.get(0).text());
//            System.out.println(ss.get(1).text());
               // System.out.println(ss.get(0).text() + "==" + ss.get(1).text());
            }
        }
        return list;
    }

    /**
     *author:  zhouwei
     *time:  2020/3/19
     *function: 获取古诗词数据信息
     */
    public static List getTanshi(String url){
        /**
         *  https://so.gushiwen.org/gushi/xiaoxue.aspx
         *  https://so.gushiwen.org/shiwenv_ef9cd9ba44bb.aspx
         */
        List list=new ArrayList();
        Iterator it=findTanShi(url).iterator();
        TangShi ts=null;
        int i=0;
        while (it.hasNext()){
            String obj=(String)it.next();
            String [] tss=obj.split("=");
            ts=new TangShi();
           //System.out.println(tss[2]);
            //注意有些需要加入https://so.gushiwen.org
            String urls="";
            if(tss[2].indexOf("https")>=0){

                urls=tss[2];
            }else{
                urls="https://so.gushiwen.org"+tss[2];
            }
           // System.out.println(urls);
            Document doc=  getDocument(urls);
            Elements elements=doc.getElementsByAttributeValue("class","contson");
           // System.out.println(elements.text());
            Elements elements1=doc.getElementsByAttributeValue("class","contyishang");
           // System.out.println(elements1.size());
            if(elements1.size()>0) {
                //获取中注释解释
                String t = elements1.get(0).getElementsByTag("p").text();
                //String t1= elements1.get(1).getElementsByTag("p").text();
                String[] s = t.split("注释");
               if(s.length>1) {
                   //System.out.println(s[0]);
                   //System.out.println("注释"+s[1]);
                   ts.setNj(tss[0]);
                   ts.setName(tss[1]);
                   ts.setContent(elements.get(0).text());
                   ts.setYw(s[0] + "");
                   ts.setZs("注释" + s[1] + "");

                   //System.out.println(ts.getName()+"="+ts.getContent());
               }else{
                   ts.setNj(tss[0]);
                   ts.setName(tss[1]);
                   ts.setContent(elements.get(0).text());
                   ts.setYw(s[0] + "");
               }
                list.add(ts);

               // System.out.println(s[0]);
            }

          /*  try {
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }
        System.out.println("========="+list.size());
        return list;
    }

    /**
     *author:  zhouwei
     *time:  2020/3/18
     *function: 爬虫爬去各种年纪古诗词
     */
    public static Set findTanShi(String url){
        Document doc=  getDocument(url);

        System.out.println("成功");
        //doc.body()
        //第二步处理
        /**
         * 提取url
         */
        Set set=new HashSet();
        try {
            // Document doc = Jsoup.parse(res);
            //获取body
            Elements ss = doc.getElementsByAttributeValue("class", "sons");

            Elements ss1= ss.get(0).getElementsByClass("typecont");

            for(int i=0;i<ss1.size();i++){
                String html= ss1.get(i).html();
                Document hdoc=Jsoup.parse(html);
                Elements elements=hdoc.body().getAllElements();
                String title=elements.get(0).getElementsByAttributeValue("class","bookMl").text();
                // System.out.println(elements.get(0).tagName("div").html());
//               System.out.println("**********");
//               System.out.println(elements.get(0));
//               System.out.println("**********");
//                System.out.println(title);
                //Elements elements1= elements.get(0).getElementsByAttributeValue("target","_blank");
                Elements elements1=elements.get(0).getElementsByTag("span");
                for(int j=0;j<elements1.size();j++){
                    String val=title+"="+elements1.get(j).text()+"="+elements1.get(j).getElementsByTag("a").attr("href");
                   //System.out.println(val);
                    set.add(val);
                }

            }

            return  set;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http://ip.taobao.com/service/getIpInfo.php?ip=111.178.151.11
     * 淘宝接口查询ip地址信息
     * @param requestUrl
     * @return
     */

    public static String sendGetUrl(String requestUrl) {
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
                //System.out.println(res);
                //object= JSON.parseObject(res);
                return res;
            } else {
                throw new Exception("连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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

    public static Document getDocument (String url){
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
