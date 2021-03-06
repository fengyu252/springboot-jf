package com.zhouwei.springboot.mapper;


import com.zhouwei.springboot.entity.SjTask;
import com.zhouwei.springboot.test.M;

import java.util.List;
import java.util.Map;

public interface SjTaskMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_task
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_task
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int insert(SjTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_task
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int insertSelective(SjTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_task
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    SjTask selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_task
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int updateByPrimaryKeySelective(SjTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_task
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int updateByPrimaryKey(SjTask record);

    /**
     * 查询当日任务信息
     */
    List findNowDay(Map mp);

    /**
     * 查询所有务信息
     */
    List findTaskMess(Map mp);
    /**
     * 查询当月任务信息
     */
    List findTaskAdmin(Map mp);
    /**
     * 统计报表信息
     */
    List totalTaskMess(Map mp);

}