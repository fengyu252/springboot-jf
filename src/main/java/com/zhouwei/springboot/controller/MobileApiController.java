package com.zhouwei.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.zhouwei.springboot.entity.*;
import com.zhouwei.springboot.server.SjTaskServer;
import com.zhouwei.springboot.utils.IpSourceUtil;
import com.zhouwei.springboot.utils.ListResult;
import com.zhouwei.springboot.utils.RedisClient;
import com.zhouwei.springboot.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 *author:  zhouwei
 *time:  2020/4/28
 *function: api 请求接口信息
 */
@RequestMapping("/sjmobile")
@RestController
public class MobileApiController {

    Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    RedisClient redisClient;
    @Autowired
    SjTaskServer sjTaskServer;//任务
    @Autowired
    ListResult listResult;
    @Autowired
    IpSourceUtil ipSourceUtil;
    /**
     * 根据手机编号获取任务id
     */
    @GetMapping(value={"/findNumberTaskId","/findSjTaskIphoneByIphoneNum"})
    public SjTaskIphone findNumberTaskId(String iphoneNum){
        logger.info("MobileApiController-findNumberTaskId-"+iphoneNum);
        if(iphoneNum.isEmpty()){
            SjTaskIphone sjTaskIphone=new SjTaskIphone();
            sjTaskIphone.setIphonenumber(0);
            sjTaskIphone.setTaskid("0");
            sjTaskIphone.setR1("iphone is null (iphoneNum)"+ iphoneNum);
            return sjTaskIphone;
        }
        //查询缓存中信息
        Object obj=redisClient.hmget(RedisKeyUtil.IPHONE_HASH_API,iphoneNum);
        if(obj!=null&&!"".equals(obj)) {
            SjTaskIphone sjTaskIphone= JSON.parseObject(obj.toString(), SjTaskIphone.class);
            return sjTaskIphone;
        }else{
           // sjTaskServer.fin
            List<SjTaskIphone> list=sjTaskServer.findTaskIdByIphoneNum(Integer.parseInt(iphoneNum));
            if(list!=null) {
                String s=JSON.toJSONString(list.get(0));
                Map rmap=new HashMap();
                rmap.put(iphoneNum,s);
                redisClient.hmset(RedisKeyUtil.IPHONE_HASH_API,rmap,RedisKeyUtil.IPHONE_HASH_TIME_API);
                return list.get(0);
            }
        }
        return null;
    }
    /**
     * 根据任务ID获取信息
     */
    @GetMapping(value={"/findTask"})
    public SjTask findTask(Integer taskId){
        logger.info("MobileApiController-findTask-"+taskId);
        try {
            //走缓存中获取任务信息
            Object obj = redisClient.hmget(RedisKeyUtil.TASK_HASH, taskId.toString());
            if (obj != null) {
                SjTask sjTask = JSON.parseObject(obj.toString(), SjTask.class);
                //获取关键词信息
                Map<Object, Object> objg = redisClient.hmget(RedisKeyUtil.backGJC_HASH(taskId.toString()));
                if (objg != null&&!objg.isEmpty()) {
                    List<SjTaskGjcList> lg = objg.entrySet().stream()
                            .sorted(Comparator.comparing(g -> g.getKey().toString()))
                            .map(g -> new SjTaskGjcList(g.getKey().toString(),g.getValue().toString()))
                            .collect(Collectors.toList());
                    Map mp=new HashMap();
                    mp.put("bType",sjTask.getTasktype());
                    mp.put("gIds","0");
                    //进行随即排序
                    Collections.shuffle(lg);
                    String sb="";
                    String gjc="";
                    gjc=lg.stream().map(
                            g -> {boolean b=g.getGjc().isEmpty();
                        if(b){
                            return null;
                        }else{
                            return g.getGjc();
                        }
                    }).filter(Objects::nonNull).collect(Collectors.joining(","));
                    sb=lg.stream().map(g -> {boolean b=g.getId().toString().isEmpty();
                        if(b){
                            return null;
                        }else{
                            return g.getId().toString();
                        }
                    })
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(","));

                    mp.put("gIds",sb.toString());
                    List<SjTaskDetail> ld=sjTaskServer.findSjDetailGjc(mp);
                    if(!ld.isEmpty()){

                         gjc=lg.stream().map(
                                g -> {List<SjTaskDetail> ress=ld.stream().
                                        filter(d -> g.getId() == Integer.parseInt(d.getR3())
                                                && d.getR5()!=null&&
                                                g.getXnumber() > Integer.parseInt(d.getR5()))
                                        .collect(Collectors.toList());
                                    if(ress.size()>0) {
                                        return g.getGjc();
                                    }else{
                                        return null;
                                    }
                                }
                        ).filter(Objects::nonNull).
                                 sorted().
                                 collect(Collectors.joining(","));
                       // gjc=gjc.substring(0,gjc.lastIndexOf(","));

                    }
                    sjTask.setGjc(gjc);

                }
                return sjTask;
            }else{
               //缓存中不存在
               SjTask sjTask= sjTaskServer.findTaskById(taskId);
               if(sjTask!=null){
                   String s=JSON.toJSONString(sjTask);
                   //SjTask sj= JSON.parseObject(s,SjTask.class);
                   Map<String,Object> rmap=new HashMap<String,Object>();
                   rmap.put(sjTask.getId()+"",s);
                   redisClient.hmset(RedisKeyUtil.TASK_HASH,rmap,RedisKeyUtil.TASK_HASH_TIME);
                   List<SjTaskGjcList> sjTaskGjcLists=sjTaskServer.findGjcByTaskId(taskId);
                   if(sjTaskGjcLists!=null&&sjTaskGjcLists.size()>0){
                       Collections.shuffle(sjTaskGjcLists);
                       for(int i=0,len=sjTaskGjcLists.size();i<len;i++){
                           String ss=  JSON.toJSONString(sjTaskGjcLists.get(i));
                           Map rmapp=new HashMap();
                           rmap.put(sjTaskGjcLists.get(i).getId().toString(),s);
                           redisClient.hmset(RedisKeyUtil.backGJC_HASH(sjTaskGjcLists.get(i).getTaskid().toString()),rmapp,RedisKeyUtil.GJC_HASH_TIME);
                       }

                   }

               }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加详情信息
     */
    @GetMapping(value={"addSJTaskDetail"})
    public ListResult addSJTaskDetail(String idfa,String ip,String bundleId,
                                      String names,String appStoreId,String cusId,String model,
                                      String oss,String taskId,String r1,String r3,String r4,String r5,String r6){
        logger.info("MobileApiController-addSJTaskDetail-");
        try {
            names = new String(names.getBytes("ISO8859-1"), "UTF-8");
            r1 = new String(r1.getBytes("ISO8859-1"), "UTF-8");
            model = new String(model.getBytes("ISO8859-1"), "UTF-8");
            SjTaskDetail sjTaskDetail=new SjTaskDetail();
            sjTaskDetail.setIdfa(idfa);
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sjTaskDetail.setChangtime(sf.format(new Date()));
            sjTaskDetail.setBundleid(bundleId);
            sjTaskDetail.setNames(names);
            sjTaskDetail.setIp(ip);
            sjTaskDetail.setAppstoreid(appStoreId);
            sjTaskDetail.setCusid(cusId);
            sjTaskDetail.setModel(model);
            sjTaskDetail.setOss(oss);
            sjTaskDetail.setTaskid(taskId);
            sjTaskDetail.setR1(r1);
            sjTaskDetail.setR3(r3);
            sjTaskDetail.setR4(r4);
            sjTaskDetail.setR5(r5);
            sjTaskDetail.setR6(r6);
            //idfa 排重
            Map mp = new HashMap();
            mp.put("taskId",taskId);
            mp.put("idfa",idfa);
            mp.put("type",1);
           int count= sjTaskServer.findIpPc(mp);
           if(count>0){
               listResult.setCode(8);
               listResult.setMess("idfa exists ");
               return listResult;
           }
          int row=  sjTaskServer.addSjDetailMes(sjTaskDetail);
          if(row>0){
              //读取任务数量
              //走缓存中获取任务信息
              Object ob = redisClient.hmget(RedisKeyUtil.TASK_HASH, taskId.toString());
              if(ob!=null){
                  SjTask sjTask = JSON.parseObject(ob.toString(), SjTask.class);
                  //获取关键词信息
                  Map<Object, Object> gob = redisClient.hmget(RedisKeyUtil.backGJC_HASH(taskId.toString()));
                  if (gob != null&&!gob.isEmpty()) {
                      List<SjTaskGjcList> lgg = gob.entrySet().stream()
                              .sorted(Comparator.comparing(g -> g.getKey().toString()))
                              .map(g -> new SjTaskGjcList(g.getKey().toString(), g.getValue().toString()))
                              .collect(Collectors.toList());

                      mp.put("bType", sjTask.getTasktype());
                      mp.put("gIds", "0");
                      //进行随即排序
                      Collections.shuffle(lgg);
                      String sb ="";
                      String gjc = "";
                      sb=lgg.stream().map(g -> {boolean b=g.getId().toString().isEmpty();
                          if(b){
                              return null;
                          }else{
                              return g.getId().toString();
                          }
                      })
                              .filter(Objects::nonNull)
                              .collect(Collectors.joining(","));
                      mp.put("gIds",sb);
                      List<SjTaskDetail> ld=sjTaskServer.findSjDetailGjc(mp);
                      //总共跑的量
                      IntSummaryStatistics s=   lgg.stream().mapToInt(
                              g->{List b=ld.stream()
                                      .filter(d ->
                                              Integer.parseInt(d.getR5())>=g.getXnumber()).collect(Collectors.toList());
                                  if (b!=null&&b.size()>0){
                                      return g.getXnumber();
                                  }else{
                                      return 0;
                                  }
                              }
                      ).filter(Objects::nonNull).summaryStatistics();
                      //判读任务是否完成了
                      if(s.getSum()<sjTask.getNumber()){
                        //判断关键词是否完成
                          //实际跑的数量
                          List<SjTaskDetail> list=  ld.stream().filter(d-> d.getR3().equals(r3)).collect(Collectors.toList());
                         //任务数量
                          List<SjTaskGjcList> listg=lgg.stream().filter(g-> r3.equals(g.getId().toString())).collect(Collectors.toList());
                              int sjnumber=0,number=0;

                              if(list!=null&&list.size()>0) {
                              sjnumber = Integer.parseInt(list.get(0).getR5());
                              }
                              if(listg!=null&&listg.size()>0){
                                 number=listg.get(0).getXnumber();
                              }else{
                                  listResult.setCode(-10);
                                  listResult.setMess(" redis filter gjc or detail failed");
                                  return listResult;
                              }
                              if(sjnumber>number){
                                  listResult.setCode(6);
                                  listResult.setMess(r1+"gjc task is over");
                                  return listResult;
                              }else{
                                  listResult.setCode(0);
                                  listResult.setMess("gjc task is go");
                                  return listResult;
                              }

                      }else {
                          //任务完成
                          listResult.setCode(4);
                          listResult.setMess("task is over number");
                          return listResult;
                      }
                  }else{
                      //关键词缓存失败
                      listResult.setCode(-301);
                      listResult.setMess("gjc redis not found ");
                  }
              }else{
                  //任务缓存失败
                  listResult.setCode(-201);
                  listResult.setMess("task redis  not found");
              }
          }else{
              //插入数据报错
              listResult.setCode(-101);
              listResult.setMess("task insert fail");
          }
        }catch (Exception e){
            listResult.setCode(2);
            listResult.setMess("Exception mess");
        }
        sjTaskServer.addSJMethodMess("","addSJTaskDetail",
                r4,ip,taskId,idfa,listResult.getMess()+"="+listResult.getCode());
        return listResult;
    }
    /**
     * IP 排重
     */
    @GetMapping(value={"findSjIpPc"})
    public ListResult findSjIpPc(String taskId,String bId,String ip){
        logger.info("MobileApiController-findSjIpPc-"+taskId+","+bId+","+ip);
        try{
            HashMap map=new HashMap();
            map.put("type",0);
            map.put("bId",bId);
            map.put("ip",ip);
           int count= sjTaskServer.findIpPc(map);
           if(count>3){
               listResult.setCode(-1);//
               listResult.setMess("ip  exists");
           }else{
               listResult.setCode(0);
               listResult.setMess("sucess ");
           }

        }catch (Exception e){
           // e.printStackTrace();

            listResult.setCode(-2);
            listResult.setMess("Exception");
        }
        return listResult;
    }

    /**
     * 删除详情信息
     */
    @GetMapping(value={"delSjTaskDetail"})
    public ListResult delSjDetailIdfa(String taskId,String idfa){
        logger.info("MobileApiController-delSjDetailIdfa-"+taskId+"-idfa="+idfa);
        try {
            HashMap map = new HashMap();
            map.put("taskId", taskId);
            map.put("idfa", idfa);
            int row = sjTaskServer.delIdfa(map);
            if (row > 0)
                listResult.setCode(0);
        }catch (Exception e){
            e.printStackTrace();
            listResult.setCode(-2);
        }
        return listResult;
    }

    /**
     * 获取IP 信息
     */
    @GetMapping(value={"findIPMess"})
    public Map findIPMess(String taskId,String bId){
        logger.info("MobileApiController-findIPMess-"+taskId+"-bId="+bId);
        Map p=new HashMap();
        p.put("ip","");//ip
        p.put("port","");//port
        p.put("name","");//帐号
        p.put("pass","");//密码
        p.put("state",1);//状态 0 成功 1 失败 2异常
        p.put("mess","");
        p.put("bId",bId);
        p.put("taskId",taskId);
        p.put("httpType",0);
        p.put("type",0);
        try{
            while(true){
               Object obj= redisClient.get(RedisKeyUtil.IP_LIST_SET);
               if(obj!=null){
                    List<SjIpSource> list= JSON.parseArray(obj.toString(),SjIpSource.class);
                    list= list.stream().filter(j->j.getStates()==0).collect(Collectors.toList());
                    if(list!=null&&list.size()>0){
                        SjIpSource sjIpSource = list.get(0);
                        if(sjIpSource.getStates()==0) {
                            Map pp = ipSourceUtil.getIp(sjIpSource.getType(), sjIpSource.getUrl());
                            sjTaskServer.addSJMethodMess(sjIpSource.getUrl(),"findIPMess",
                                    "","",taskId,"",p.get("mess")+"="+p.get("state"));
                            if (Integer.parseInt(pp.get("code") + "") == 0) {
                                //ip 排重
                                p.put("ip", pp.get("ip"));//ip
                                p.put("port", pp.get("port"));//port
                                p.put("name", sjIpSource.getName());//帐号
                                p.put("pass", sjIpSource.getPass());//密码
                                p.put("httpType", Integer.parseInt(sjIpSource.getR1()));//http 类型
                                int row = sjTaskServer.findIpPc(p);
                                if (row > 3) {
                                    continue;
                                } else {
                                    p.put("state", 0);//状态 0 成功 1 失败 2异常
                                    p.put("mess", "成功" + sjIpSource.getMsg());
                                    break;
                                }
                            } else {
                                p.put("state", 1);
                                p.put("mess", sjIpSource.getMsg() + "|code:" + pp.get("code") + "|msg:" + pp.get("mess"));
                                break;
                            }
                        }else{
                            p.put("state", 666);//状态 0 成功 1 失败 2异常 4
                            p.put("mess", "ip源已经关闭");
                            break;
                        }
                    }else{
                        p.put("state", 666);//状态 0 成功 1 失败 2异常 4
                        p.put("mess", "数据库中没有ip源信息");
                        break;
                    }
               }else{
                   p.put("state", 666);//状态 0 成功 1 失败 2异常 4
                   p.put("mess", " redis数据库中没有ip源信息");
                   break;
               }
            }
        }catch (Exception e){
            p.put("state",-2);
            p.put("mess","异常");
        }

        return p;
    }

    /**
     * 回调方法
     */
    @RequestMapping(value={"/callbackIdfa/{taskId}/{idfa}"})
    public String callbackIdfa(@PathVariable("taskId") Integer taskId, @PathVariable("idfa") String idfa){
        logger.info("MobileApiController-callbackIdfa-"+taskId+"=idfa-"+idfa);
        Map mp=new HashMap();
        mp.put("taskId",taskId);
        mp.put("idfa",idfa);

        try{
            sjTaskServer.addSJMethodMess("","callbackIdfa",
                    "","",taskId+"",idfa,"");
            if(taskId>0){
                //表示异步操作
                sjTaskServer.updateCallBackMess(mp);
            }else{
                logger.info("callbackIdfa 任务Id taksId="+taskId);
            }
        }catch (Exception e){
            logger.info("callbackIdfa 出现异常"+e.getMessage());
        }
        return "{success:true}";
    }

    /**
     * 添加url 连接
     * @return
     */
    @RequestMapping(value={"/addSjQdGetUrl"})
    public ListResult addSjQdGetUrl(Integer qtype,Integer ttype,
                                    String taskId,String url,String r1){
        logger.info("MobileApiController-callbackIdfa-"+taskId);
        try {
            SjQdGetUrl sjQdGetUrl = new SjQdGetUrl();
            sjQdGetUrl.setQtype(qtype);
            sjQdGetUrl.setTtype(ttype);
            sjQdGetUrl.setTaskid(taskId);
            sjQdGetUrl.setUrl(URLDecoder.decode(url, "utf-8"));
            sjQdGetUrl.setChangtime(new Date());
            if (!r1.isEmpty()) {
                r1 = URLDecoder.decode(r1, "utf-8");
                if (r1.length() > 1000) {
                    sjQdGetUrl.setR1(r1.substring(0, 300));
                } else {
                    sjQdGetUrl.setR1(r1);
                }
            } else {
                sjQdGetUrl.setR1(r1);
            }
            sjTaskServer.addSjQdGetUrl(sjQdGetUrl);
            listResult.setCode(0);
            listResult.setMess("success");
        }catch (Exception e){
            e.printStackTrace();
        }
        return listResult;
    }

    /**
     * 添加激活信息
     * @param udid
     * @return
     */
    @GetMapping("/addIphoneActive")
    public String addIphoneActive(@RequestParam("udid") String udid){
        LocalDateTime localDateTime = LocalDateTime.now();
        Map<String,Object> mp=new HashMap<>();
        mp.put(udid, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        redisClient.hmset(RedisKeyUtil.IPHONE_LIVE,mp,RedisKeyUtil.IPHONE_LIVE_TIME);
        return "ok";
    }

}
