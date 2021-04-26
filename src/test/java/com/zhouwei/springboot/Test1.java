package com.zhouwei.springboot;

import com.zhouwei.springboot.utils.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test1 {

    @Autowired
    RedisClient redisClient;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void redisSetNx(){
        boolean b= redisTemplate.opsForValue().setIfAbsent("zhouwei","1234");
        if(b){
            System.out.println("boolean : success");
        }else{
            System.out.println("boolean : failed");

        }

    }
}
