package com.zhouwei.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *@创建人  zhouwei
 *@创建时间  2019/12/12
 *@参数
 *@返回值  
 */
@Configuration
public class DruidConfig {

    private final static String userName="root";
    private final static String passWrod="root";
    //配置数据源
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    /**
     * 配置Druid 监控
     * 1：配置一个servlet 后台监控
     * 2: 配置一个filter
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean =
                new ServletRegistrationBean<StatViewServlet>(new StatViewServlet(),"/druid/*");
        //配置访问密码帐号信息
        Map<String,String> initParam=new HashMap<>();
        initParam.put(StatViewServlet.PARAM_NAME_USERNAME,userName);
        initParam.put(StatViewServlet.PARAM_NAME_PASSWORD,passWrod);
        //允许访问ip
        initParam.put(StatViewServlet.PARAM_NAME_ALLOW,"");
        //禁止访问ip
        initParam.put(StatViewServlet.PARAM_NAME_DENY,"");
        bean.setInitParameters(initParam);
        return  bean;
    }
    /**
     * 配置一个监控
     */
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        //配置访问拦截
        Map<String,String> initParam=new HashMap<>();
        initParam.put(WebStatFilter.PARAM_NAME_EXCLUSIONS,"*.js,*.css,/druid/*");
        bean.setInitParameters(initParam);
        //拦截所有请求
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;

    }

}
