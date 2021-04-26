package com.zhouwei.springboot.entity;

import java.io.Serializable;

public class TangShi implements Serializable {

    private String nj;
    private String name;
    private String content;
    private String yw;
    private String zs;
    private String imgUrl;
    private String type="-1";



    public TangShi() {}

    public TangShi(String nj, String name, String content, String yw, String zs, String imgUrl, String type) {
        this.nj = nj;
        this.name = name;
        this.content = content;
        this.yw = yw;
        this.zs = zs;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYw() {
        return yw;
    }

    public void setYw(String yw) {
        this.yw = yw;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }



    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
