package com.zhouwei.springboot.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;
/**
 *author:  zhouwei
 *time:  2020/5/11
 *function: 设置redis 中json 不乱码的格式
 */
@Configuration
public class RedisConfig {

    @Primary
    @Bean
    public RedisTemplate<Object, Object> jsonRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //template.setDefaultSerializer(new Jackson2JsonRedisSerializer(Object.class));
        template.setDefaultSerializer(new FastJsonRedisSerializer(Object.class));
        template.setConnectionFactory(redisConnectionFactory);
        // 设置value的序列化规则和 key的序列化规则
//        template.setValueSerializer(new FastJsonRedisSerializer(Object.class);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.afterPropertiesSet();
        return template;
    }
}
