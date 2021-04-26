package com.zhouwei.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync//开启异步操作
@MapperScan(value = "com.zhouwei.springboot.mapper")//扫描包
@SpringBootApplication
public class Springboot_jf {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Springboot_jf.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
