package com.zhouwei.springboot.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
/**
 *创建人  zhouwei
 *创建时间  2020/1/6
 * 返回同意结果集
 */
@Component
public class ListResult {

    private Integer code=-100;
    private String mess="no mess";
    private Object data;
    private Object data1;

    public ListResult() {
    }

    public ListResult(Integer code) {
        this.code = code;
    }

    public ListResult(Integer code, String mess) {
        this.code = code;
        this.mess = mess;
    }

    public ListResult(Integer code, String mess, Object data) {
        this.code = code;
        this.mess = mess;
        this.data = data;
    }

    public ListResult(Integer code, String mess, Object data, Object date1) {
        this.code = code;
        this.mess = mess;
        this.data = data;
        this.data1 = data1;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData1() {
        return data1;
    }

    public void setData1(Object data1) {
        this.data1 = data1;
    }

    public String toJsonString(){
        return JSON.toJSONString(this);
    }

    public ListResult formt(String json){
        try {
            return JSON.parseObject(json, ListResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
