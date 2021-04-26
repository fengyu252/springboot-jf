package com.zhouwei.springboot.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ip 来源地址信息
 */
@Component
public class IpSourceUtil {

    @Autowired
    RedisClient redisClient;
    /**
     * 豌豆http
     *{"code":200,"msg":"ok","data":[{"ip":"117.23.122.104","port":39828,"expire_time":"2019-10-30 10:10:27","city":"\u5b89\u5eb7","isp":"\u7535\u4fe1"}]}
    */
     public static Map getWandouIp(String url){
        Map p=new HashMap();
        p.put("ip","");
        p.put("port","");
        p.put("code",1);
        p.put("mess","");

        // url="http://api.wandoudl.com/api/ip?app_key=4f9a68c3cfd3b1b0d68030f5a6e5126d&pack=207907&num=1&xy=3&type=2&mr=1&";
        try{
            JSONObject res=HttpGetOrPostUtil.sendGetUrl(url);
            if(res!=null){
                //JSONObject json=JSONObject.parseObject(UnicodeUtil.unicodetoString(res.toString()));
                if(res.containsKey("code")) {
                    int code =res .getIntValue("code");
                    if (code == 200) {
                        JSONArray jr = res.getJSONArray("data");
                        JSONObject ipdata = jr.getJSONObject(0);
                        String ip = ipdata.getString("ip");
                        String port = ipdata.getString("port");
                        String city = ipdata.getString("city");
                        String isp = ipdata.getString("isp");//电信 联通 移动
                        p.put("ip", ip);
                        p.put("city", "");//UnicodeUtil.unicodetoString(city));
                        p.put("isp", isp);
                        p.put("port", port);
                        p.put("code", 0);
                        p.put("mess", "成功");
                    } else {
                        p.put("code", 1);
                        p.put("mess", "返回json 不为200 " + code + "|msg:" + res.getString("msg"));
                    }
                }else{
                    p.put("code",1);
                    p.put("mess","获取json 为空 没有code");
                }
            }else{
                p.put("code",1);
                p.put("mess","获取json 为空");
            }
        }catch (Exception e){

            p.put("code",2);
        }
        return p;
    }

    /**
     * 豌豆http
     *{"code":200,"msg":"ok","data":[{"ip":"117.23.122.104","port":39828,"expire_time":"2019-10-30 10:10:27","city":"\u5b89\u5eb7","isp":"\u7535\u4fe1"}]}
     */
    public  Map getWandouIpBigNumberSet(String url){
        List<Map<String,Object>> list=new ArrayList<>();
        Map p=new HashMap();
        p.put("ip","");
        p.put("port","");
        p.put("code",1);
        p.put("mess","");
        p.put("isp", "");
        p.put("mess", "");
        try{
            Long num  =redisClient.sGetSetSize("ipSource");
          // num=  RedisPoolUtil.scard("ipSource");
            if(num>5){//判读数据源中ip 大于某个数字

            }else {
                JSONObject res = HttpGetOrPostUtil.sendGetUrl(url);
                if (res != null) {
                    //JSONObject json=JSONObject.parseObject(UnicodeUtil.unicodetoString(res.toString()));
                    if (res.containsKey("code")) {
                        int code = res.getIntValue("code");
                        if (code == 200) {
                            JSONArray jr = res.getJSONArray("data");
                            Map pp = null;
                            for (int j = 0, size = jr.size(); j < size; j++) {
                                pp = new HashMap();
                                pp.put("ip", "");
                                pp.put("port", "");
                                pp.put("city", "");
                                pp.put("code", 1);
                                pp.put("isp", "");
                                pp.put("mess", "");
                                JSONObject ipdata = jr.getJSONObject(j);
                                String ip = ipdata.getString("ip");
                                String port = ipdata.getString("port");
                                String city = ipdata.getString("city");
                                String isp = ipdata.getString("isp");//电信 联通 移动
                                pp.put("ip", ip);
                                pp.put("city", "");
                                pp.put("isp", isp);
                                pp.put("port", port);
                                pp.put("code", 0);
                                pp.put("mess", "成功");
                                list.add(pp);
                            }
                            if(list!=null&&list.size()>0){
                                redisClient.sSet("ipSource",600,list);
                            }

                        } else {
                            p.put("code", 1);
                            p.put("mess", "返回json 不为200 " + code + "|msg:" + res.getString("msg"));
                        }
                    } else {
                        p.put("code", 1);
                        p.put("mess", "获取json 为空 没有code");
                    }
                } else {
                    p.put("code", 1);
                    p.put("mess", "获取json 为空");
                }
            }
            String ipdata=  redisClient.sGetSetPop("ipSource");
            if(!"nil".equals(ipdata)){
                JSONObject json= JSONObject.parseObject(ipdata);
                p.put("ip", json.getString("ip"));
                p.put("port", json.getString("port"));
                p.put("city", json.getString("city"));
                p.put("code", 0);
                p.put("isp", json.getString("isp"));
                p.put("mess", json.getString("mess"));
            }
        }catch (Exception e){

            p.put("code",2);
        }
        return p;
    }



    /**
     * 太阳http代理
     * code 121 套餐用完了
     * {"msg":"0","code":0,"data":[{"city":"浙江省丽水市","port":"4332","ip":"115.213.202.244","isp":"电信"}],"success":true}
     */
    public   Map getTaiyanIp(String url){
        Map p=new HashMap();
        p.put("ip","");
        p.put("port","");
        p.put("code",1);
        p.put("mess","");
         //url="http://http.tiqu.qingjuhe.cn/getip?num=1&type=2&pack=40257&port=2&ys=1&cs=1&lb=1&pb=4&regions=150000,210000,330000,360000,410000,620000";
        try{
            JSONObject res=HttpGetOrPostUtil.sendGetUrl(url);
            if(res!=null){
                //JSONObject json=JSONObject.parseObject(UnicodeUtil.unicodetoString(res.toString()));
                if(res.containsKey("code")) {
                    int code = res.getIntValue("code");
                    if (code == 0) {
                        JSONArray jr = res.getJSONArray("data");
                        JSONObject ipdata = jr.getJSONObject(0);
                        String ip = ipdata.getString("ip");
                        String port = ipdata.getString("port");
                        String city = ipdata.getString("city");
                        String isp = ipdata.getString("isp");//电信 联通 移动
                        p.put("ip", ip);
                        p.put("city", city);
                        p.put("isp", isp);
                        p.put("port", port);
                        p.put("code", 0);
                        p.put("mess", "成功");
                    } else {
                        p.put("code", 1);
                        p.put("mess", "返回json 不为200|code: " + code + "|msg:" + res.getString("msg"));
                    }
                }else{
                    p.put("code",1);
                    p.put("mess","获取json 为空 没有code");
                }
            }else{
                p.put("code",1);
                p.put("mess","获取json 为空");
            }
        }catch (Exception e){

            p.put("code",2);
        }
        return p;
    }

    /**
     * 蜻蜓代理
     *
     */
    public   Map getQintingIp(String url) {
        Map p=new HashMap();
        p.put("ip","");
        p.put("port","");
        p.put("code",1);
        p.put("mess","");
        // url="http://api.qingtingip.com/ip?app_key=fd1dc66cdfda01cd36a4a0710980f57e&num=1&ptc=socks5&fmt=json&port=0&mr=1";
        try{
            JSONObject res=HttpGetOrPostUtil.sendGetUrl(url);
            if(res!=null){
                //JSONObject json=JSONObject.parseObject(UnicodeUtil.unicodetoString(res.toString()));
                if(res.containsKey("code")) {
                    int code = res.getIntValue("code");
                    if (code == 200) {
                        JSONArray jr = res.getJSONArray("data");
                        JSONObject ipdata = jr.getJSONObject(0);
                        String ip = ipdata.getString("ip");
                        String port = ipdata.getString("port");
                        String city = ipdata.getString("city");
                        String isp = ipdata.getString("isp");//电信 联通 移动
                        p.put("ip", ip);
                        p.put("city", "");//UnicodeUtil.unicodetoString(city));
                        p.put("isp", isp);
                        p.put("port", port);
                        p.put("code", 0);
                        p.put("mess", "成功");
                    } else {
                        p.put("code", 1);
                        p.put("mess", "返回json 不为200|code: " + code + "|msg:" + res.getString("msg"));
                    }
                }else{
                    p.put("code",1);
                    p.put("mess","获取json 为空 没有code");
                }
            }else{
                p.put("code",1);
                p.put("mess","获取json 为空");
            }
        }catch (Exception e){

            p.put("code",2);
        }
        return p;
    }

    /**
     * http://http.zhiliandaili.cn/Index-getapi.html
     * 智联httP
     * @param
     * @param url
     * @return
     */
    public   Map getZhilianIp(String url) {
        Map p = new HashMap();
        p.put("ip", "");
        p.put("port", "");
        p.put("code", 1);
        p.put("mess", "");
        try{
            JSONObject res=HttpGetOrPostUtil.sendGetUrl(url);
            if(res!=null){
                //JSONObject json=JSONObject.parseObject(UnicodeUtil.unicodetoString(res.toString()));
                if(res.containsKey("code")) {
                    int code = res.getIntValue("code");
                    if (code == 0) {
                        JSONArray jr = res.getJSONArray("data");
                        JSONObject ipdata = jr.getJSONObject(0);
                        String ip = ipdata.getString("IP");
                        String port = ipdata.getString("Port");
                        String city = ipdata.getString("IpAddress");
                        String isp = ipdata.getString("ISP");//电信 联通 移动
                        p.put("ip", ip);
                        p.put("city", city);
                        p.put("isp", isp);
                        p.put("port", port);
                        p.put("code", 0);
                        p.put("mess", "成功");
                    } else {
                        p.put("code", 1);
                        p.put("mess", "返回json 不为200|code: " + code + "|msg:" + res.getString("msg"));
                    }
                }else{
                    p.put("code",1);
                    p.put("mess","获取json 为空 没有code");
                }
            }else{
                p.put("code",1);
                p.put("mess","获取json 为空");
            }
        }catch (Exception e){

            p.put("code",2);
        }
        return p;
    }

    /**
     * 黑洞代理http
     * 471515353 pass 471515353
     * https://www.ipjldl.com/Index-getapi.html
     * @param
     * @param url
     * @return
     */
    public   Map getHeidongIp(String url) {
        Map p = new HashMap();
        p.put("ip", "");
        p.put("port", "");
        p.put("code", 1);
        p.put("mess", "");
        try{
            JSONObject res=HttpGetOrPostUtil.sendGetUrl(url);
            System.out.println("===="+res);
            if(res!=null){
                //JSONObject json=JSONObject.parseObject(UnicodeUtil.unicodetoString(res.toString()));
                if(res.containsKey("ret")) {
                    int ret = res.getIntValue("ret");
                    System.out.println(ret);
                    if (ret == 200) {
                        JSONObject jr = res.getJSONObject("data");
                        JSONObject list = jr.getJSONObject("list");
                        int code = jr.getIntValue("code");
                        if(code ==0) {
                            JSONArray jrr = list.getJSONArray("ProxyIpInfoList");
                            JSONObject ipdatal = jrr.getJSONObject(0);
                            String ip = ipdatal.getString("IP");
                            String port = ipdatal.getString("Port");
                            String city = ipdatal.getString("IpAddress");
                            String isp = ipdatal.getString("ISP");//电信 联通 移动
                            p.put("ip", ip);
                            p.put("city", city);
                            p.put("isp", isp);
                            p.put("port", port);
                            p.put("code", 0);
                            p.put("mess", "成功");
                        }else{
                            p.put("code", 1);
                            p.put("mess", "返回json code 不为0|code: " + code + "|msg:" + res.getString("data"));

                        }
                    } else {
                        p.put("code", 1);
                        p.put("mess", "返回json 不为200|code: " + ret + "|msg:" + res.getString("data"));
                    }
                }else{
                    p.put("code",1);
                    p.put("mess","获取json 为空 没有code");
                }
            }else{
                p.put("code",1);
                p.put("mess","获取json 为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            p.put("code",2);
        }
        return p;
    }

    /**
     *
     * 66代理
     * @param
     * @param url
     * @return
     */
    public   Map get66Ip(String url) {
        Map p = new HashMap();
        p.put("ip", "");
        p.put("port", "");
        p.put("code", 1);
        p.put("mess", "");
        try{
            JSONObject res=HttpGetOrPostUtil.sendGetUrl(url);
            if(res!=null){
                //JSONObject json=JSONObject.parseObject(UnicodeUtil.unicodetoString(res.toString()));
                if(res.containsKey("status")) {
                    String code = res.getString("status")+"";
                    if ("true".equals(code)) {
                       String proxies  =res.getString("proxies")+"";
                        proxies= proxies.replace("[","").replace("]","").replace("\"","");
                       String pp [] =proxies.split(":");
                        //System.out.println(proxies);

                        String ip =pp[0]; //ipdata.getString("IP");
                        String port =pp[1];// ipdata.getString("Port");

                        p.put("ip", ip);
                        p.put("city", "");
                        p.put("isp", "");
                        p.put("port", port);
                        p.put("code", 0);
                        p.put("mess", "成功");
                    } else {
                        p.put("code", 1);
                        p.put("mess", "返回json 不为200|code: " + code + "|msg:" + res.getString("msg"));
                    }
                }else{
                    p.put("code",1);
                    p.put("mess","获取json 为空 没有code");
                }
            }else{
                p.put("code",1);
                p.put("mess","获取json 为空");
            }
        }catch (Exception e){

            p.put("code",2);
        }
        return p;
    }

    public   Map getIp(int type,String url){
        Map p=null;
       switch (type){
           case 1://豌豆http
              p = getWandouIpBigNumberSet(url);
              //p= getWandouIpBigNumberList(url);
              break;
           case 2://蜻蜓http
               p = getQintingIp(url);
               break;
           case 3://太阳http
               p = getTaiyanIp(url);
               break;
           case 4://智联http
               p = getZhilianIp(url);
               break;
           case 5://黑洞http
               p = getHeidongIp(url);
               break;
           case 6://66http
               p = get66Ip(url);
               break;
           default:
               p = getWandouIp(url);
               break;
       }
       return p;
    }

    public static void main(String[] args) {
        String url="http://api.wandoudl.com/api/ip?app_key=4f9a68c3cfd3b1b0d6803" +
                "0f5a6e5126d&pack=208092&num=1&xy=3&type=2&mr=1&";

//        Map p=getIp(1,url);
//        System.out.println(p.toString());
    }

}
