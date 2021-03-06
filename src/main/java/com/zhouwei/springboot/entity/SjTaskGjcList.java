package com.zhouwei.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class SjTaskGjcList {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.id
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.taskId
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private Integer taskid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.gjc
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private String gjc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.xnumber
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private Integer xnumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.sjnumber
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private Integer sjnumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.changeTime
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private String changetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.r1
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private String r1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.r2
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private String r2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.r3
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private String r3;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.r4
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private String r4;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sj_task_gjc_list.r5
     *
     * @mbggenerated Tue Mar 24 14:55:14 CST 2020
     */
    private String r5;

    public SjTaskGjcList() {
    }


    public SjTaskGjcList(String key, String val){
        SjTaskGjcList sjTaskGjcList= JSON.parseObject(val,SjTaskGjcList.class);
        this.id = sjTaskGjcList.getId();
        this.taskid = sjTaskGjcList.getTaskid();
        this.gjc = sjTaskGjcList.getGjc();
        this.xnumber = sjTaskGjcList.getXnumber();
        this.sjnumber = sjTaskGjcList.getSjnumber();
        this.changetime = sjTaskGjcList.getChangetime();
        this.r1 = sjTaskGjcList.getR1();
        this.r2 = sjTaskGjcList.getR2();
        this.r3 = sjTaskGjcList.getR3();
        this.r4 = sjTaskGjcList.getR3();
        this.r5 = sjTaskGjcList.getR5();
    }

    public SjTaskGjcList(Integer id, Integer taskid, String gjc,
                         Integer xnumber, Integer sjnumber, String changetime,
                         String r1, String r2, String r3, String r4, String r5) {
        this.id = id;
        this.taskid = taskid;
        this.gjc = gjc;
        this.xnumber = xnumber;
        this.sjnumber = sjnumber;
        this.changetime = changetime;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
    }
}