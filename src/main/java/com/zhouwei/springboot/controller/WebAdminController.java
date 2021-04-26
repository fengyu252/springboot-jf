package com.zhouwei.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.zhouwei.springboot.entity.*;
import com.zhouwei.springboot.server.SjTaskServer;
import com.zhouwei.springboot.server.SjTaskTotalServer;
import com.zhouwei.springboot.utils.*;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/webAdmin")
public class WebAdminController {
    Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    RedisClient redisClient;
    @Autowired
    SjTaskServer sjTaskServer;//任务
    @Autowired
    SjTaskTotalServer sjTaskTotalServer;
    @Autowired
    ListResult listResult;


    @RequestMapping(value = {"/goLogin","/login"})
    public String goLogin() {
        return "login";
    }

    @GetMapping(value = {"/goIndex"})
    public String goIndex() {
        logger.info("WebAdminController-webAdmin-goIndex-");
        return "html/main";
    }

    @GetMapping(value = {"/goTaskTable"})
    public String goTaskTable(HttpServletRequest request,HttpSession session,Map map) {
        Map mp=new HashMap();
        try{
            /**
             * java 8 LocalDateTime  和DateTimeFormatter格式转换
             */
            LocalDateTime localDateTime = LocalDateTime.now();
               // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                mp.put("changeTime",  localDateTime.toLocalDate());
                logger.info("WebAdminController-webAdmin-goTaskTable-" + mp.get("changeTime"));
                List<SjTask> list = sjTaskServer.findNowDay(mp);
                if (list != null && list.size() > 0) {
                    map.put("list", list);
                    //request.setAttribute("list",list);
                } else {
                    map.put("list", null);
                    //request.setAttribute("list",null);
                }

        }catch (Exception e){
            logger.info("WebAdminController-webAdmin-goTaskTable-"+e.getMessage());
        }

        return "html/taskTable";
    }


    /**
     *author:  zhouwei
     *time:  2020/4/20
     *function: 查询当月任务
     */

    @GetMapping(value = {"/goTableAdmin"})
    public String goTableAdmin(Map map){
        Map mp=new HashMap();
        try{
            //判读缓存中是否存在

            logger.info("WebAdminController-webAdmin-goTableAdmin-"+mp.get("page"));
            List<SjTask> list=  sjTaskServer.findTaskAdmin(mp);
            if(list!=null&&list.size()>0){
                map.put("list",list);
            }else{
                map.put("list",null);

            }
        }catch (Exception e){
            logger.info("WebAdminController-webAdmin-goTableAdmin-"+e.getMessage());
        }
        return "html/taskTableAdmin";
    }
    /**
     *author:  zhouwei
     *time:  2020/4/20
     *function: 查询历史信息
     */
    @GetMapping(value = {"/goTableHistory/{page}"})
    public String goTableHistory( @PathVariable("page") Integer page,Map map){
        Map mp=new HashMap();
        Integer pages=1;
        try{
            logger.info("WebAdminController-webAdmin-goTableHistory-"+mp.get("page"));
            if(mp.containsKey("page")){
                pages=Integer.parseInt(mp.get("page").toString());
            }
            if(page>0){
                pages=page;
            }
            mp.put("page",pages);
            mp.put("pageNum",30);
            map.put("startPage",pages);
            map.put("nextPage",pages+1);
            //加入缓存当中
            Object s= redisClient.get(RedisKeyUtil.TASK_HISTORY);
            List<SjTask> list=null;
            if(s!=null&&!"".equals(s)){
            list= JSON.parseArray(s.toString(),SjTask.class);

                map.put("list",list);
            }else{
                list = sjTaskTotalServer.findSjTotal(mp);
                map.put("list",list);
                if(list!=null){
                    redisClient.set(RedisKeyUtil.TASK_HISTORY,JSON.toJSON(list),RedisKeyUtil.TASK_HISTORY_TIME);
                }
            }
        }catch (Exception e){
           e.printStackTrace();
        }
        return "html/taskTableHistory";
    }


    @GetMapping(value={"/goTableAdd"})
    public String goTableAdd(HttpSession session,Map map){
        try{

               String name= session.getAttribute("loginUser").toString();
               String pass=session.getAttribute("pass").toString();
               session.setAttribute("tokenGoTableAdd", JwtTokenUtil.createToken(name+""+pass,"goTableAdd"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return "html/taskAdd";
    }

    /**
     * 任务添加信息
     * @param session
     * @param sjTask
     * @return
     */
    @PostMapping(value={"/taskAddMess"})
    public String taskAddMess(HttpSession session,SjTask sjTask){
        logger.info("WebAdminController-webAdmin-taskAddMess-"+sjTask);
        try {

            Object tokens= session.getAttribute("tokenGoTableAdd");
            String token=null;
            boolean b=false;
            if(tokens!=null) {//
                token = tokens.toString();
                b = JwtTokenUtil.verifyToken(token);

            }
            if(b) {
                sjTask.setChangetime(new Date());
                int row = sjTaskServer.addTaskMess(sjTask);
                if (row > 0) {
                    logger.info("taskAddMess:"+row+"==="+sjTask.getId());
                    //添加到
                    String s=JSON.toJSONString(sjTask);
                   //SjTask sj= JSON.parseObject(s,SjTask.class);
                    Map<String,Object> rmap=new HashMap<String,Object>();
                    rmap.put(sjTask.getId()+"",s);
                    redisClient.hmset(RedisKeyUtil.TASK_HASH,rmap,RedisKeyUtil.TASK_HASH_TIME);
                    return "redirect:/webAdmin/goTaskTable";
                } else {
                    //return "redirect:/webAdmin/goTaskTable";
                    logger.info("taskAddMess:添加信息失败");
                }
                session.removeAttribute("tokenGoTableAdd");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/webAdmin/goTaskTable";
    }

    @GetMapping(value="/goTaskCopy/{id}")
    public String goTaskCopy(@PathVariable("id") Integer id,Map mp,HttpSession session){
        logger.info("WebAdminController-webAdmin-goTaskCopy-"+id);
        try{
            String name= session.getAttribute("loginUser").toString();
            String pass=session.getAttribute("pass").toString();
            session.setAttribute("tokenGoTableAdd", JwtTokenUtil.createToken(name+""+pass,"goTableAdd"));

            SjTask sjTask=null;
            if(id>0) {
                sjTask = sjTaskServer.findTaskById(id);
            }
            mp.put("sjTask",sjTask);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "html/taskAdd";
    }

    @GetMapping(value="/goTaskUpdate/{id}")
    public String goTaskUpdate(@PathVariable("id") Integer id,Map mp){
        logger.info("WebAdminController-webAdmin-goTaskUpdate-"+id);
        try{

            SjTask sjTask=null;
            if(id>0) {
                //查询缓存中是否存在
              Object task=  redisClient.hmget(RedisKeyUtil.TASK_HASH,id+"");
              logger.info("WebAdminController-webAdmin-taskUpdateMess-"+task);
              if(task!=null){
                  sjTask=JSON.parseObject(task.toString(),SjTask.class);
              }else{
                  sjTask = sjTaskServer.findTaskById(id);
              }

            }
            mp.put("sjTask",sjTask);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "html/taskUpdate";
    }
    /**
     *author:  zhouwei
     *time:  2020/4/14
     *function: 修改任务信息
     */
    @PutMapping(value = {"/taskUpdateMess"})
    public String taskUpdateMess(SjTask sjTask){
        logger.info("WebAdminController-webAdmin-taskUpdateMess-"+sjTask);
        try{

           int row= sjTaskServer.updateTaskMess(sjTask);
           if(row>0){

               //存入缓存当中
               String s=JSON.toJSONString(sjTask);
               //SjTask sj= JSON.parseObject(s,SjTask.class);
               Map rmap=new HashMap();
               rmap.put(sjTask.getId().toString(),s);
               redisClient.hmset(RedisKeyUtil.TASK_HASH,rmap,RedisKeyUtil.TASK_HASH_TIME);

               return "redirect:/webAdmin/goTaskTable";
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/webAdmin/goTaskTable";
    }


    @GetMapping(value={"delTaskById/{id}"})
    public String delTaskById(@PathVariable("id") Integer id){
        logger.info("WebAdminController-webAdmin-delTaskById-"+id);
      try {
          int  row=sjTaskServer.delSjTaskById(id);
          if(row>0){
              //删除任务信息
              redisClient.hdel(RedisKeyUtil.TASK_HASH,id+"");
              //删除关键词信息
              redisClient.del(RedisKeyUtil.backGJC_HASH(id.toString()));
          }
      }catch (Exception e){
          e.printStackTrace();
      }
       return  "redirect:/webAdmin/goTaskTable";
    }
    /**
     *
     * @param
     * @return
     * 查看任务信息
     */
    @GetMapping(value={"/goTaskDetail/{bType}/{taskId}/{gjcId}/{lType}"})
    public String goTaskDetail(@PathVariable("taskId") Integer taskId,
                               @PathVariable("bType") Integer bType,
                               @PathVariable("gjcId") Integer gjcId,
                               @PathVariable("lType") Integer lType,
                               Map mp){
        logger.info("WebAdminController-webAdmin-goTaskDetail-"+taskId+"-"+bType);
        try{
            List<SjTaskDetail> list=null;
            if(taskId>0) {
                list=sjTaskServer.findSjDetailMess(taskId,bType,lType,gjcId);
            }
            mp.put("sjDetail",list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "html/taskDetailMess";
    }

    /**
     *  进入关键词列表
     *
     * @return
     */
    @GetMapping(value={"/goGjcTable/{taskId}"})
    public String goGjcTable(@PathVariable("taskId") Integer taskId,Map mp){
        logger.info("WebAdminController-webAdmin-goGjcTable-"+taskId+"-");
        try {
           if (taskId > 0) {
               List<SjTaskGjcList> list=sjTaskServer.findGjcByTaskId(taskId);
              // logger.info("====="+list.get(0).getChangetime());
               mp.put("list",list);
               mp.put("taskId",taskId);
           }else{
               mp.put("list",null);
           }
       }catch (Exception e){
           e.printStackTrace();
       }

        return "html/gjc/taskGjcTable";
    }

    @GetMapping(value={"/goGjcAdd"})
    public String goGjcAdd(Map mp,Integer taskId){
        logger.info("WebAdminController-webAdmin-goGjcAdd-"+taskId);
        mp.put("taskId",taskId);
        //做一些查询
        return "html/gjc/taskGjcAdd";
    }

    @PostMapping(value={"addGjc"})
    public String addGjc(SjTaskGjcList sjTaskGjcList){
        logger.info("WebAdminController-webAdmin-goGjcAdd-");
        try{
            LocalDateTime localDateTime=LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            sjTaskGjcList.setChangetime(dateTimeFormatter.format(localDateTime));
           int row= sjTaskServer.addGjcMess(sjTaskGjcList);
           if(row>0){
              String s=  JSON.toJSONString(sjTaskGjcList);
              Map rmap=new HashMap();
              rmap.put(sjTaskGjcList.getId().toString(),s);
              redisClient.hmset(RedisKeyUtil.backGJC_HASH(sjTaskGjcList.getTaskid().toString()),rmap,RedisKeyUtil.GJC_HASH_TIME);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/webAdmin/goGjcTable/"+sjTaskGjcList.getTaskid();
    }

    @GetMapping(value={"/goUpdateGjc/{id}/{taskId}"})
    public String goUpdateGjc(@PathVariable("id") Integer id,@PathVariable("taskId") Integer taskId,Map mp){
       if(id>0){
          Object obj=redisClient.hmget(RedisKeyUtil.backGJC_HASH(taskId.toString()),id+"");
           SjTaskGjcList sjTaskGjcList=JSON.parseObject(obj.toString(),SjTaskGjcList.class);
           if(sjTaskGjcList==null){
               sjTaskGjcList=sjTaskServer.findGjcById(id);
           }
           mp.put("gjc",sjTaskGjcList);
       }else{
           mp.put("gjc",null);
       }
        return "html/gjc/taskGjcUpdate";
    }

    @PutMapping(value={"/updateGjc"})
    public String updateGjc(SjTaskGjcList gjc){
        logger.info("WebAdminController-webAdmin-updateGjc-");
       try{
           int row= sjTaskServer.updateGjcMess(gjc);
           if(row>0){
               String s=  JSON.toJSONString(gjc);
               Map rmap=new HashMap();
               rmap.put(gjc.getId().toString(),s);
               redisClient.hmset(RedisKeyUtil.backGJC_HASH(gjc.getTaskid().toString()),rmap,RedisKeyUtil.GJC_HASH_TIME);
           }
       }catch (Exception e){
           e.printStackTrace();
       }
        return "redirect:/webAdmin/goGjcTable/"+gjc.getTaskid();
    }

    @GetMapping(value={"/delGjcId/{taskId}/{id}"})
    public String delGjcId(@PathVariable("taskId") Integer taskId,
                           @PathVariable("id") Integer id){
        logger.info("WebAdminController-webAdmin-delGjcId-"+taskId+"-");
        try{
            int row=sjTaskServer.delGjcById(id);
            if(row>0){
                redisClient.hdel(RedisKeyUtil.backGJC_HASH(taskId.toString()),id+"");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/webAdmin/goGjcTable/"+taskId;
    }

    /**
     * 进入手机编号页面
     */
    @GetMapping(value={"/goSjTaskIphone/{lType}"})
    public String  goSjTaskIphone(@PathVariable(value = "lType") Integer lType,Map mp){
        logger.info("WebAdminController-webAdmin-goSjTaskIphone-");
        Map map=new HashMap();
        map.put("lType",lType);//0
        List<SjTaskIphone> list=sjTaskServer.findSJIphoneByR2(map);
        if(!list.isEmpty()){
            mp.put("list",list);
        }else{
            mp.put("list",null);
        }
        return "html/num/taskIphonetable";
    }

    @GetMapping(value={"/goSjTaskIphoneAdd"})
    public String goSjTaskIphoneAdd(){
        return "html/num/taskIphoneAdd";
    }

    @PostMapping(value={"/sjTaskIphoneAdd"})
    public String sjTaskIphoneAdd(SjTaskIphone sjTaskIphone){
        logger.info("WebAdminController-webAdmin-sjTaskIphoneAdd-");
        //添加
        if(sjTaskIphone.getIphonenumber()!=null){
            //日期格式转换
            LocalDateTime localDateTime=LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            sjTaskIphone.setChangetime(dateTimeFormatter.format(localDateTime));
           int row= sjTaskServer.addSJIphone(sjTaskIphone);
           if(row>0){
               String s=JSON.toJSONString(sjTaskIphone);
               Map rmap=new HashMap();
               rmap.put(sjTaskIphone.getId().toString(),s);
               redisClient.hmset(RedisKeyUtil.IPHONE_HASH,rmap,RedisKeyUtil.IPHONE_HASH_TIME);
               redisClient.hset(RedisKeyUtil.IPHONE_HASH_API,sjTaskIphone.getIphonenumber().toString(),s,RedisKeyUtil.IPHONE_HASH_TIME_API);
           }
        }
        return "redirect:/webAdmin/goSjTaskIphone/0";
    }


    @GetMapping(value={"/goSjTaskIphoneUpdate/{id}"})
    public String goSjTaskIphoneUpdate(@PathVariable("id") Integer id,Map mp){
        logger.info("WebAdminController-webAdmin-sjTaskIphoneAdd-"+id);
        if(id>0) {
            SjTaskIphone sjTaskIphone=null;
            Object obj=redisClient.hmget(RedisKeyUtil.IPHONE_HASH,id.toString());
            if(obj!=null&&!"".equals(obj)) {
                 sjTaskIphone=JSON.parseObject(obj.toString(),SjTaskIphone.class);
            }else{
                 sjTaskIphone = sjTaskServer.findSJIphoneById(id);
            }
            mp.put("iphone", sjTaskIphone);
        }else{
            mp.put("iphone",null);
        }
        return "html/num/taskIphoneUpdate";
    }

    @PutMapping(value={"/sjTaskIphoneUpdate"})
    public String sjTaskIphoneUpdate(SjTaskIphone sjTaskIphone){
        logger.info("WebAdminController-webAdmin-sjTaskIphoneUpdate-"+sjTaskIphone.getId());
        if(sjTaskIphone.getId()!=null&&sjTaskIphone.getId()>0) {
            //日期格式转换
            LocalDateTime localDateTime=LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            sjTaskIphone.setChangetime(dateTimeFormatter.format(localDateTime));
           int row=sjTaskServer.updateSjTaskIphone(sjTaskIphone);
           if(row>0){
               String s=JSON.toJSONString(sjTaskIphone);
               Map rmap=new HashMap();
               rmap.put(sjTaskIphone.getId().toString(),s);
               redisClient.hmset(RedisKeyUtil.IPHONE_HASH,rmap,RedisKeyUtil.IPHONE_HASH_TIME);
               redisClient.hset(RedisKeyUtil.IPHONE_HASH_API,sjTaskIphone.getIphonenumber().toString(),s,RedisKeyUtil.IPHONE_HASH_TIME_API);

           }
        }
        return "redirect:/webAdmin/goSjTaskIphone/0";
    }

    @GetMapping(value={"/updateSjTaskIphoneZero/{id}"})
    public String updateSjTaskIphoneZero(@PathVariable("id") Integer id){
        logger.info("WebAdminController-webAdmin-updateSjTaskIphoneZero-"+id);
        int row=sjTaskServer.updateSjTaskIphoneNumById(id);
        if(row>0){
            Object obj=redisClient.hmget(RedisKeyUtil.IPHONE_HASH,id.toString());
            if(obj!=null&&!"".equals(obj)) {
                SjTaskIphone sjTaskIphone=JSON.parseObject(obj.toString(),SjTaskIphone.class);
                sjTaskIphone.setTaskid("0");
                String s=  JSON.toJSONString(sjTaskIphone);
                Map rmap=new HashMap();
                rmap.put(sjTaskIphone.getId().toString(),s);
                redisClient.hmset(RedisKeyUtil.IPHONE_HASH,rmap,RedisKeyUtil.IPHONE_HASH_TIME);
                redisClient.hset(RedisKeyUtil.IPHONE_HASH_API,sjTaskIphone.getIphonenumber().toString(),s,RedisKeyUtil.IPHONE_HASH_TIME_API);

            }
        }
        return "redirect:/webAdmin/goSjTaskIphone/0";
    }

    @GetMapping(value={"/updateSjTaskIphoneYc/{id}/{type}"})
    public String updateSjTaskIphoneYc(@PathVariable("id") Integer id,@PathVariable("type") Integer type){
        logger.info("WebAdminController-webAdmin-updateSjTaskIphoneYc-"+id);
        int row=sjTaskServer.updateSjIphoneR2ById(id,type);//1 隐藏 0显示
        if(row>0){
            Object obj=redisClient.hmget(RedisKeyUtil.IPHONE_HASH,id.toString());
            if(obj!=null&&!"".equals(obj)) {
                SjTaskIphone sjTaskIphone=JSON.parseObject(obj.toString(),SjTaskIphone.class);
                sjTaskIphone.setR2(type+"");
                String s=  JSON.toJSONString(sjTaskIphone);
                Map rmap=new HashMap();
                rmap.put(sjTaskIphone.getId().toString(),s);
                redisClient.hmset(RedisKeyUtil.IPHONE_HASH,rmap,RedisKeyUtil.IPHONE_HASH_TIME);
                redisClient.hset(RedisKeyUtil.IPHONE_HASH_API,sjTaskIphone.getIphonenumber().toString(),s,RedisKeyUtil.IPHONE_HASH_TIME_API);

            }
        }
        return "redirect:/webAdmin/goSjTaskIphone/0";
    }

    @GetMapping(value={"/delSjTaskIphoneById/{id}"})
    public String delSjTaskIphoneById(@PathVariable("id") Integer id){
        logger.info("WebAdminController-webAdmin-delSjTaskIphonById-"+id);
        int row= sjTaskServer.delSjIphoneById(id);//1 隐藏 0显示
        if(row>0){
            redisClient.hdel(RedisKeyUtil.IPHONE_HASH,id.toString());
            redisClient.del(RedisKeyUtil.IPHONE_HASH_API);
        }
        return "redirect:/webAdmin/goSjTaskIphone/0";
    }

    @GetMapping(value={"/updateSjTaskIphoneAllZero"})
    public String updateSjTaskIphoneAllZero(){
        logger.info("WebAdminController-webAdmin-updateSjTaskIphoneAllZero-");
        int row=sjTaskServer.updateTaskIdAllZero(0);//1 隐藏 0显示
        if(row>0){
            Map rmap=new HashMap();
           Map map= redisClient.hmget(RedisKeyUtil.IPHONE_HASH);
           Iterator iterator=map.entrySet().iterator();
           while(iterator.hasNext()){
               Map.Entry  p=(Map.Entry) iterator.next();
               String key=p.getKey().toString();
               String val=p.getValue().toString();
               if(!val.isEmpty()){
                   SjTaskIphone sjTaskIphone= JSON.parseObject(val,SjTaskIphone.class);
                   sjTaskIphone.setTaskid("0");

                   String s= JSON.toJSONString(sjTaskIphone);
                   rmap.put(key,s);
               }
           }
           if(rmap.size()>0)
            redisClient.hmset(RedisKeyUtil.IPHONE_HASH,rmap,RedisKeyUtil.IPHONE_HASH_TIME);
            redisClient.del(RedisKeyUtil.IPHONE_HASH_API);

        }
        return "redirect:/webAdmin/goSjTaskIphone/0";
    }

    @GetMapping(value={"/goIpTable"})
    public String goIpTable(Map mp){
        logger.info("WebAdminController-webAdmin-goIpTable-");
        //查询任务
        List<SjIpSource> lt=null;
        Object  obj= redisClient.get(RedisKeyUtil.IP_LIST_SET);
        if(obj!=null&&!"".equals(obj)){
            lt=JSON.parseArray(obj.toString(),SjIpSource.class);
            mp.put("ip",lt);
        }else{
             lt=sjTaskServer.findAllIp(0);
            mp.put("ip",lt);
           String s= JSON.toJSONString(lt);
            redisClient.set(RedisKeyUtil.IP_LIST_SET,s);
        }
        return "html/ip/taskIpTable";
    }

    @GetMapping(value={"/goIpAdd"})
    public String goIpAdd(){
        logger.info("WebAdminController-webAdmin-goIpAdd-");

        return "html/ip/taskIpAdd";
    }

    @GetMapping(value={"/goIpUpdate/{id}"})
    public String goIpUpdate(@PathVariable("id") Integer id,Map mp){
        logger.info("WebAdminController-webAdmin-goIpUpdate-");
        if(id>0){
           SjIpSource sjIpSource= sjTaskServer.findSjIpSourceById(id);

            mp.put("ip",sjIpSource);
        }else{
            mp.put("ip",null);
        }
        return "html/ip/taskIpUpdate";
    }

    @PostMapping(value={"/addIpSource"})
    public String addIpSource(SjIpSource sjIpSource){
        logger.info("WebAdminController-webAdmin-addIpSource-");
        if(sjIpSource.getName()!=null) {

            sjIpSource.setChangtime(new Date());
            sjTaskServer.addIpSource(sjIpSource);
            redisClient.del(RedisKeyUtil.IP_LIST_SET);
        }
        return "redirect:/webAdmin/goIpTable";
    }

    @PutMapping(value={"/updateIpSource"})
    public String updateIpSource(SjIpSource sjIpSource){
        logger.info("WebAdminController-webAdmin-addIpSource-");
        if(sjIpSource.getName()!=null) {
            sjIpSource.setChangtime(new Date());
            sjTaskServer.updateIpSource(sjIpSource);
            redisClient.del(RedisKeyUtil.IP_LIST_SET);
        }
        return "redirect:/webAdmin/goIpTable";
    }

    @GetMapping(value={"/delIpSource/{id}"})
    public String delIpSource(@PathVariable("id") Integer id){
        logger.info("WebAdminController-webAdmin-delIpSource-");
        if(id>0) {
            sjTaskServer.delIpSource(id);
            redisClient.del(RedisKeyUtil.IP_LIST_SET);
        }
        return "redirect:/webAdmin/goIpTable";
    }

    @GetMapping(value={"/updateIpSourceStates/{id}/{type}"})
    public String updateIpSourceStates(@PathVariable("id") Integer id,
            @PathVariable("type") Integer type){
        logger.info("WebAdminController-webAdmin-updateIpSourceStates-");
        if(id>0) {
            sjTaskServer.updateIpSourceStates(id, type);
            redisClient.del(RedisKeyUtil.IP_LIST_SET);
        }
        return "redirect:/webAdmin/goIpTable";
    }

    @GetMapping(value={"/goLuaTable/{type}"})
    public String goLuaTable(Map mp,@PathVariable("type") Integer type){
        logger.info("WebAdminController-webAdmin-goIpTable-");
        List<SjLua> list= sjTaskServer.findAllLua(type);
        if(!list.isEmpty()){
            mp.put("lua",list);
        }else{
            mp.put("lua",list);
        }
        return "html/lua/taskLuaTable";
    }

    @GetMapping(value={"/goLuaAdd"})
    public String goLuaAdd(){
        logger.info("WebAdminController-webAdmin-goLuaAdd-");

        return "html/lua/taskLuaAdd";
    }

    @GetMapping(value={"/goLuaUpdate/{id}"})
    public String goLuaUpdate(Map mp,@PathVariable("id") Integer id){
        logger.info("WebAdminController-webAdmin-goLuaUpdate-");
        if(id>0){
            SjLua sjLua= sjTaskServer.findAllLuaById(id);
            mp.put("lua",sjLua);
        }else{
            mp.put("lua",null);
        }
        return "html/lua/taskLuaUpdate";
    }

    @GetMapping(value={"/delLua/{id}"})
    public String delLua(Map mp,@PathVariable("id") Integer id){
        logger.info("WebAdminController-webAdmin-delLua-");
        if(id>0){
            sjTaskServer.delLuaById(id);

        }
       return  "redirect:/webAdmin/goLuaTable/0";
    }

    @PostMapping(value={"/addLua"})
    public String addLua(SjLua sjLua){
        logger.info("WebAdminController-webAdmin-addLua-");
        if(sjLua.getName()!=null){
            sjTaskServer.addLua(sjLua);
        }
        return  "redirect:/webAdmin/goLuaTable/0";
    }

    @PutMapping(value={"/updateLua"})
    public String updateLua(SjLua sjLua){
        logger.info("WebAdminController-webAdmin-updateLua-");
        if(sjLua.getName()!=null){
            sjTaskServer.updateLua(sjLua);
        }
        return  "redirect:/webAdmin/goLuaTable/0";
    }

    @GetMapping(value={"/updateLuaByType/{id}/{type}"})
    public String updateLuaByType(@PathVariable("id") Integer id,@PathVariable("type") Integer type){
        logger.info("WebAdminController-webAdmin-updateLuaByType-");
        if(id>0){
            sjTaskServer.updateLuaTypeById(id,type);
        }
        return  "redirect:/webAdmin/goLuaTable/0";
    }

    @RequestMapping(value = {"/loginAdmin"})
    public String loginAdmin(String name,String pass,HttpSession session,Map mp) {
        try {
            if("zhouwei".equals(name)&&"888888".equals(pass)){
                session.setAttribute("pass", pass);
                session.setAttribute("loginUser", name);
                session.setMaxInactiveInterval(60 * 60);
                //创建token
                session.setAttribute("token", JwtTokenUtil.createToken(name,pass));
            }else{
                mp.put("msg","密码或者用户名错误");
                return "login";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/webAdmin/goIndex";
    }

    @GetMapping(value={"/totalTaskMess/{d1}/{d2}"})
    public String totalTaskMess(Map mp,@PathVariable("d1") String d1,@PathVariable("d2") String d2){
        logger.info("WebAdminController-webAdmin-totalTaskMess-");
        Map map=new HashMap();
        if("0".equals(d1)){
            map.put("type",0);
            map.put("d1",DateLocalTimeUtil.javaDate2String(new Date(),DateLocalTimeUtil.FORMAT_YEAR_MONTH2));
            map.put("d2",null);
        }else{
            map.put("type",1);
            map.put("d1",d1);
            map.put("d2",d2);
        }
        List<SjTask> list= sjTaskServer.totalTaskMess(map);
       // IntSummaryStatistics s= list.stream().collect(Collectors.summarizingInt(SjTask::getSjnumber));
        IntSummaryStatistics s=  list.stream().filter(sjTask -> sjTask.getTasktype()==0).mapToInt(SjTask::getSjnumber).summaryStatistics();
        IntSummaryStatistics sh=  list.stream().filter(sjTask -> sjTask.getTasktype()==1).mapToInt(SjTask::getSjnumber).summaryStatistics();
        //IntSummaryStatistics stats = list.stream().mapToInt( -> x).summaryStatistics();
        List<SjTask> lt=new ArrayList<SjTask>();
        String d11="";
        SjTask sjTask=null;
        for(int i=0,len=list.size();i<len;i++){
           String d22= DateLocalTimeUtil.javaDate2String(list.get(i).getChangetime(),DateLocalTimeUtil.FORMAT_YEAR_MONTH);
           if(i==0)
             d11=d22;
           if(!d11.equals("")&&d22.equals(d11)){
               lt.add(list.get(i));
               if(i==len-1){
                   sjTask=new SjTask();
                   sjTask.setR3(list.get(i-1).getR1()+"/"+list.get(i-1).getR2());
                   sjTask.setTasktype(2);
                   lt.add(sjTask);

               }

           }else{
               sjTask=new SjTask();
               sjTask.setR3(list.get(i-1).getR1()+"/"+list.get(i-1).getR2());
               sjTask.setTasktype(2);
               lt.add(sjTask);
               lt.add(list.get(i));
               d11=d22;
           }

        }
        mp.put("list",lt);
       mp.put("ssum",s.getSum());
       mp.put("hsum",sh.getSum());
       mp.put("sum",s.getSum()+sh.getSum());
        return "html/total/taskTotalTable";
    }

    @GetMapping(value={"/taskTotalM"})
    public String taskTotalM(Map mp){
        HashMap map=new HashMap();
        LocalDateTime localDateTime = LocalDateTime.now();
        map.put("changeTime",  localDateTime.toLocalDate());
        logger.info("WebAdminController-webAdmin-taskTotalM-" + mp.get("changeTime"));
        List<SjTask> list = sjTaskServer.findNowDay(map);
        if (list != null && list.size() > 0) {
            mp.put("list", list);
            //request.setAttribute("list",list);
        } else {
            mp.put("list", null);
            //request.setAttribute("list",null);
        }
        return "html/total/taskTotalMTable";
    }


    @GetMapping(value={"/taskTotalTj/{type}/{taskId}/{taskType}"})
    public String taskTotalTj(Map mp,
    @PathVariable("type") Integer type,@PathVariable("taskId") Integer taskId,
                             @PathVariable("taskType") Integer taskType){
        HashMap map=new HashMap();
        map.put("taskType",taskType);
        map.put("taskId",taskId);
        List<SjTaskDetail> list=null;
        if(type==1){
           list= sjTaskServer.totalMTask(map);
        }else if(type==2){
             list=  sjTaskServer.totalHTask(map);
        }else{}
        mp.put("list",list);
        return "html/total/taskTotalTjTable";
    }
    @GetMapping(value={"/goExlUpload"})
    public String  goExlUpload(){
        return "html/file/exlUpload";
    }

    @GetMapping(value={"/goQdUrl/{taskId}"})
    public String findQdUrl( @PathVariable("taskId") Integer taskId,Map mp){
        if(taskId>0){
            mp.put("taskId",taskId);
           List<SjQdGetUrl> list= sjTaskServer.findQdUrl(mp);
           mp.put("list",list!=null?list:null);
        }
        return "html/qd/taskQdUrl";
    }


    @RequestMapping("/delLiveMess")
    public String  delLiveMess() {
        redisClient.del(RedisKeyUtil.IPHONE_LIVE);
       return  "redirect:/webAdmin/findLiveIphone";
    }

    @RequestMapping("/findLiveIphone")
    public String findLiveIphone(Map mpp) {
        List<SjTask> list=new ArrayList<>();
        Map<Object,Object> mp =redisClient.hmget(RedisKeyUtil.IPHONE_LIVE);
        try {
            if (mp != null&&mp.size()>0) {

                SjTask sjTask = null;
                int i=1;
                for (Map.Entry<Object, Object> entry : mp.entrySet()) {
                    sjTask = new SjTask();
                    String val = entry.getValue()+"";
                    String key = entry.getKey()+"";
                    sjTask.setNames(val);
                    sjTask.setR1(key);
                    sjTask.setId(i);
                    i++;
                    list.add(sjTask);
                }
                mpp.put("list",list);
            }
        }catch (Exception e){

        }
        return "html/qd/iphonLive";
    }

}