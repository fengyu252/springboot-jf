package com.zhouwei.springboot.mapper;


import com.zhouwei.springboot.entity.SjLua;

import java.util.List;

public interface SjLuaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_lua
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_lua
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int insert(SjLua record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_lua
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int insertSelective(SjLua record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_lua
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    SjLua selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_lua
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int updateByPrimaryKeySelective(SjLua record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sj_lua
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    int updateByPrimaryKey(SjLua record);
    /**
     * 查询lua 脚本信息
     */
    List findAll(Integer type);

}