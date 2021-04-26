package com.zhouwei.springboot.server;

import com.zhouwei.springboot.entity.*;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;

/**
 *author:  zhouwei
 *time:  2020/3/30
 *function:
 */
public interface SjTaskServer {
    /**
     * *****************************************task
     */
    /**
     * 查询当日任务信息
     */
    List findNowDay(Map mp);

    /**
     * 查询当月任务信息
     */
    List findTaskAdmin(Map mp);

    /**
     * 查询所有务信息
     */
    List findTaskMess(Map mp);

    /**
     * 添加任务信息
     */
    int addTaskMess(SjTask sjTask);

    /**
     * 修改任务信息
     */
    int updateTaskMess(SjTask sjTask);

    /**
     * 查询任务信息
     */
    SjTask findTaskById(Integer id);

    /**
     * 删除任务信息 同时删除任务关键词 详情
     */
    int delSjTaskById(Integer id);

    /**
     * 统计报表信息
     */
    List totalTaskMess(Map mp);

    /**
     * 统计任务10分钟量
     */
    List totalMTask(Map mp);

    /**
     * 统计任务小时量
     */
    List totalHTask(Map mp);
    /**
     * *****************************************detail
     */
    /**
     * 查询任务详情
     * bType 1 表示回调
     */
    List findSjDetailMess(Integer taskId, Integer bType, Integer lType, Integer gjcId);

    /**
     * 根据关键词Id查询统计
     */
    List findSjDetailGjc(Map mp);

    /**
     * 添加详情信息
     */
    int addSjDetailMes(SjTaskDetail sjTaskDetail);

    /**
     * ip排重
     */
    int findIpPc(Map mp);

    /**
     * 删除详情
     */
    int delIdfa(Map mp);

    /**
     * 回调修改信息
     */
    void updateCallBackMess(Map mp);
    /**
     *
     * *****************************************gjc
     */
    /**
     * 根据任务id查询所有关键词
     */
    List findGjcByTaskId(Integer taskId);

    /**
     * 关键词删除
     */
    int delGjcById(Integer id);

    /**
     * 添加关键词
     */
    int addGjcMess(SjTaskGjcList sjTaskGjcList);

    /**
     * 修改关键词
     */
    int updateGjcMess(SjTaskGjcList sjTaskGjcList);

    /**
     * 查询当个关键词信息
     */
    SjTaskGjcList findGjcById(Integer id);
    /**
     * *************************************************iphoneNumber
     */
    /**
     * 查询手机编号信息
     */
    List<SjTaskIphone> findSJIphoneByR2(Map mp);

    /**
     * 查询手机编号信息
     */
    SjTaskIphone findSJIphoneById(Integer id);

    /**
     * 添加手机编号信息
     */
    int addSJIphone(SjTaskIphone sjTaskIphone);

    /**
     * 修改手机编号信息
     */
    int updateSjTaskIphone(SjTaskIphone sjTaskIphone);

    /**
     * 编号置空
     */
    int updateSjTaskIphoneNumById(Integer id);

    /**
     * 编号删除
     */
    int delSjIphoneById(Integer id);

    /**
     * 编号隐藏
     */
    int updateSjIphoneR2ById(Integer id, Integer type);

    /**
     * 编号任务全部为0
     */
    int updateTaskIdAllZero(Integer id);

    /**
     * 查询任务id 根据手机编号
     */
    List<SjTaskIphone> findTaskIdByIphoneNum(Integer iphoneNum);
    /**
     * *******************************************ip
     */
    /**
     * 查询所有IP源信息
     */
    List findAllIp(Integer id);

    /**
     * ip 源添加
     */
    int addIpSource(SjIpSource sjIpSource);

    /**
     * ip 源修改
     */
    int updateIpSource(SjIpSource sjIpSource);

    /**
     * ip 源删除
     */
    int delIpSource(Integer id);

    /**
     * ip 源开启或者禁用
     */
    int updateIpSourceStates(Integer id, Integer type);

    /**
     * ip 查询
     */
    SjIpSource findSjIpSourceById(Integer id);
    /**
     * ************************************************lua
     */
    /**
     * 查询lua 脚本信息
     */
    List findAllLua(Integer type);

    /**
     * 查询lua 脚本信息
     */
    SjLua findAllLuaById(Integer id);

    /**
     * 添加lua
     */
    int addLua(SjLua sjLua);

    /**
     * 修改lua
     */
    int updateLua(SjLua sjLua);

    /**
     * 删除Lua
     */
    int delLuaById(Integer id);

    /**
     * 禁用 启用
     */
    int updateLuaTypeById(Integer id, Integer type);

    /**
     * *******************************************sjQdGetUrl
     */
    void addSjQdGetUrl(SjQdGetUrl sjQdGetUrl);
    /**
     * 查询任务url
     */
    List findQdUrl(Map map);
    /**
     * *******************************************sjMethod
     */
    void addSJMethodMess(String url,String method,String r1,String r2,String r3,String r4,String r5 );

}