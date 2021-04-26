package com.zhouwei.springboot.entity;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String name;
    private String titleImg;
    private String address;
    private int sex;
    private String pwd;
    private String email;
    private String phone;
    private String r1;
    private String r2;
    private String r3;

    public User() {
    }

    public User(int id, String name, String titleImg, String address, int sex, String pwd, String email, String phone, String r1, String r2, String r3) {
        this.id = id;
        this.name = name;
        this.titleImg = titleImg;
        this.address = address;
        this.sex = sex;
        this.pwd = pwd;
        this.email = email;
        this.phone = phone;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
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
