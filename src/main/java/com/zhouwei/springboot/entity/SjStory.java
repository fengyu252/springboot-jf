package com.zhouwei.springboot.entity;

import java.io.Serializable;

public class SjStory implements Serializable {

    private Integer id;
    private String names;
    private Integer type=-1;
    private String  contents;
    private String imgUrl;
    private String res;
    private String xq;
    private Integer ltype;
    private String  r1;
    private String  r2;
    private String  r3;

    public SjStory() {
    }

    public SjStory(Integer id, String names, Integer type, String contents, String imgUrl, String res, String xq, Integer ltype, String r1, String r2, String r3) {
        this.id = id;
        this.names = names;
        this.type = type;
        this.contents = contents;
        this.imgUrl = imgUrl;
        this.res = res;
        this.xq = xq;
        this.ltype = ltype;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public Integer getLtype() {
        return ltype;
    }

    public void setLtype(Integer ltype) {
        this.ltype = ltype;
    }

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public String getR3() {
        return r3;
    }

    public void setR3(String r3) {
        this.r3 = r3;
    }
}
