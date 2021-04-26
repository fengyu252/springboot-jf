package com.zhouwei.springboot.server.impl;

import com.zhouwei.springboot.entity.*;
import com.zhouwei.springboot.mapper.*;
import com.zhouwei.springboot.server.SjTaskServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SjTaskServerImpl implements SjTaskServer {
    @Autowired
    private  SjTaskMapper sjTaskMapper;
    @Autowired
    private SjTaskGjcListMapper sjTaskGjcListMapper;
    @Autowired
    private SjTaskDetailMapper sjTaskDetailMapper;
    @Autowired
    private SjTaskIphoneMapper sjTaskIphoneMapper;
    @Autowired
    private SjIpSourceMapper sjIpSourceMapper;
    @Autowired
    private SjLuaMapper sjLuaMapper;
    @Autowired
    private SjQdGetUrlMapper sjQdGetUrlMapper;
    @Autowired
    private SjMethodMapper sjMethodMapper;

    @Override
    public List findNowDay(Map mp) {
        return sjTaskMapper.findNowDay(mp);
    }

    @Override
    public List findTaskMess(Map mp) {
        int page=Integer.parseInt(mp.get("page").toString());
        int pageNum=Integer.parseInt(mp.get("pageNum").toString());
        mp.put("startNum",(page-1)*pageNum);
        mp.put("pageNum",pageNum);
        return sjTaskMapper.findTaskMess(mp);
    }

    @Override
    public int addTaskMess(SjTask sjTask) {
        return sjTaskMapper.insertSelective(sjTask);
    }

    @Override
    public int updateTaskMess(SjTask sjTask) {
        return sjTaskMapper.updateByPrimaryKeySelective(sjTask);
    }

    @Override
    public SjTask findTaskById(Integer id) {
        return sjTaskMapper.selectByPrimaryKey(id);
    }
    @Transactional
    @Override
    public int delSjTaskById(Integer id) {
        try{
            sjTaskMapper.deleteByPrimaryKey(id);
            sjTaskGjcListMapper.delSjGjcListByTaaskId(id);
            sjTaskDetailMapper.delSjDetialByTaskId(id);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List findSjDetailMess(Integer taskId,Integer bType,Integer lType,Integer gjcId) {
        Map mp=new HashMap();
        mp.put("id",taskId);
        mp.put("bType",bType);
        mp.put("gjcId",gjcId);
        mp.put("lType",lType);
        return sjTaskDetailMapper.findSjDetailMess(mp);
    }

    @Override
    public List findGjcByTaskId(Integer taskId) {
        Map mp=new HashMap();
        mp.put("taskId",taskId);
        return sjTaskGjcListMapper.findGjcByTaskId(mp);
    }

    @Override
    public int delGjcById(Integer id) {
        return sjTaskGjcListMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int addGjcMess(SjTaskGjcList sjTaskGjcList) {
        return sjTaskGjcListMapper.insertSelective(sjTaskGjcList);
    }

    @Override
    public List findTaskAdmin(Map mp) {
        return sjTaskMapper.findTaskAdmin(mp);
    }

    @Override
    public List<SjTaskIphone> findSJIphoneByR2(Map mp) {
        return sjTaskIphoneMapper.findSJIphoneByR2(mp);
    }

    @Override
    public int updateGjcMess(SjTaskGjcList sjTaskGjcList) {
        return sjTaskGjcListMapper.updateByPrimaryKeySelective(sjTaskGjcList);
    }

    @Override
    public SjTaskGjcList findGjcById(Integer id) {
        return sjTaskGjcListMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addSJIphone(SjTaskIphone sjTaskIphone) {
        return sjTaskIphoneMapper.insertSelective(sjTaskIphone);
    }

    @Override
    public int updateSjTaskIphone(SjTaskIphone sjTaskIphone) {
        return sjTaskIphoneMapper.updateByPrimaryKeySelective(sjTaskIphone);
    }

    @Override
    public SjTaskIphone findSJIphoneById(Integer id) {
        return sjTaskIphoneMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateSjTaskIphoneNumById(Integer id) {
        SjTaskIphone sjTaskIphone=new SjTaskIphone();
        sjTaskIphone.setId(id);
        sjTaskIphone.setTaskid("0");
        return sjTaskIphoneMapper.updateByPrimaryKeySelective(sjTaskIphone);
    }

    @Override
    public int delSjIphoneById(Integer id) {
        return sjTaskIphoneMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateSjIphoneR2ById(Integer id, Integer type) {
        SjTaskIphone sjTaskIphone=new SjTaskIphone();
        sjTaskIphone.setId(id);
        sjTaskIphone.setR2(type+"");
        return sjTaskIphoneMapper.updateByPrimaryKeySelective(sjTaskIphone);
    }

    @Override
    public int updateTaskIdAllZero(Integer id) {
        return sjTaskIphoneMapper.updateTaskIdAllZero(id);
    }

    @Override
    public List findAllIp(Integer id) {
        return sjIpSourceMapper.findAllIp(id);
    }

    @Override
    public int addIpSource(SjIpSource sjIpSource) {
        return sjIpSourceMapper.insertSelective(sjIpSource);
    }

    @Override
    public int updateIpSource(SjIpSource sjIpSource) {
        return sjIpSourceMapper.updateByPrimaryKeySelective(sjIpSource);
    }

    @Override
    public int delIpSource(Integer id) {
        return sjIpSourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateIpSourceStates(Integer id, Integer type) {
        SjIpSource sjIpSource=new SjIpSource();
        sjIpSource.setId(id);
        sjIpSource.setStates(type);
        return sjIpSourceMapper.updateByPrimaryKeySelective(sjIpSource);
    }

    @Override
    public SjIpSource findSjIpSourceById(Integer id) {
        return sjIpSourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List findAllLua(Integer type) {
        return sjLuaMapper.findAll(type);
    }

    @Override
    public int addLua(SjLua sjLua) {
        return sjLuaMapper.insertSelective(sjLua);
    }

    @Override
    public int updateLua(SjLua sjLua) {
        return sjLuaMapper.updateByPrimaryKeySelective(sjLua);
    }

    @Override
    public int delLuaById(Integer id) {
        return sjLuaMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateLuaTypeById(Integer id, Integer type) {
        SjLua sjLua=new SjLua();
        sjLua.setId(id);
        sjLua.setType(type);
        return sjLuaMapper.updateByPrimaryKeySelective(sjLua);
    }

    @Override
    public SjLua findAllLuaById(Integer id) {
        return sjLuaMapper.selectByPrimaryKey(id);
    }

    @Override
    public List totalTaskMess(Map mp) {
        return sjTaskMapper.totalTaskMess(mp);
    }

    @Override
    public List totalMTask(Map mp) {
        return sjTaskDetailMapper.totalMTask(mp);
    }

    @Override
    public List totalHTask(Map mp) {
        return sjTaskDetailMapper.totalHTask(mp);
    }

    @Override
    public List<SjTaskIphone> findTaskIdByIphoneNum(Integer iphoneNum) {
        return sjTaskIphoneMapper.findTaskIdByIphoneNum(iphoneNum);
    }

    @Override
    public List findSjDetailGjc(Map mp) {
        return sjTaskDetailMapper.findSjDetailGjc(mp);
    }

    @Override
    public int addSjDetailMes(SjTaskDetail sjTaskDetail) {
        return sjTaskDetailMapper.insertSelective(sjTaskDetail);
    }

    @Override
    public int findIpPc(Map mp) {
        return sjTaskDetailMapper.findIpPc(mp);
    }

    @Override
    public int delIdfa(Map mp) {
        return sjTaskDetailMapper.delIdfa(mp);
    }
    @Async
    @Override
    public void updateCallBackMess(Map mp) {
        sjTaskDetailMapper.updateCallBackMess(mp);

    }
    @Async
    @Override
    public void addSjQdGetUrl(SjQdGetUrl sjQdGetUrl) {
        sjQdGetUrlMapper.insertSelective(sjQdGetUrl);
    }

    @Async
    @Override
    public void addSJMethodMess(String url,String method,String r1,String r2,String r3,String r4,String r5) {
        SjMethod sjMethod=new SjMethod();
        sjMethod.setMethod(method);
        sjMethod.setUrl(url);
        sjMethod.setChangetime(new Date());
        sjMethod.setR1(r1);
        sjMethod.setR2(r2);
        sjMethod.setR3(r3);
        sjMethod.setR4(r4);
        sjMethod.setR5(r5);
        sjMethodMapper.insertSelective(sjMethod);
    }

    @Override
    public List findQdUrl(Map map) {
        return sjQdGetUrlMapper.findQdUrl(map);
    }
}
