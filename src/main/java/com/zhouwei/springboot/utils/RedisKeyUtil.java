package com.zhouwei.springboot.utils;
/**
 *author:  zhouwei
 *time:  2020/4/29
 *function: 所有的redis key
 */
public class RedisKeyUtil {
    //任务缓存key
    public final static String TASK_HASH = "task-hash";
    //任务缓存时间time 6个小时
    public final static Integer TASK_HASH_TIME = 21600;
    //历史任务缓存key
    public final static String TASK_HISTORY = "task-history";
    //历史任务缓存time 时间
    public final static Integer TASK_HISTORY_TIME = 21600;
    //关键词缓存
    public final static String GJC_HASH = "gjc-hash-";
    //关键词缓存时间
    public final static Integer GJC_HASH_TIME = 21600;

    public static String backGJC_HASH(String key) {
        return GJC_HASH + key;
    }
    //手机编号
    public final static String IPHONE_HASH = "iphone-hash";
    public final static String IPHONE_HASH_API = "iphone-hash-API";
    public final static Integer IPHONE_HASH_TIME = 21600;
    public final static Integer IPHONE_HASH_TIME_API = 21600;
    //ip缓存
    public final static String IP_LIST_SET = "ip-hash";
    public final static Integer IP_LIST_SET_TIME = 43200;
    //每个IP缓存
    public final static String IP_SET="ip-set";
    public final static Integer IP_SET_TIME=36000;
    //手机存活时间
    public final static String IPHONE_LIVE="iphone-live";
    public final static Integer IPHONE_LIVE_TIME=6000;
}