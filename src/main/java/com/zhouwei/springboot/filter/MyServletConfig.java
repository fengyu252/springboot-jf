package com.zhouwei.springboot.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

//注册servlet主键
@Configuration
public class MyServletConfig {

    //自定义filter
    @Bean
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        //指定过滤器
        bean.setFilter(new MyFilter());
        //过滤哪些请求
        bean.setUrlPatterns(Arrays.asList("/sjmobile/findNumberTaskId"));
        return bean;
    }
    //自定义 Listener
//    @Bean
//   public  ServletListenerRegistrationBean myListener() {
//        return new ServletListenerRegistrationBean(new MyListener());
//    }


//    //注册Sevlet组件
//    @Bean
//    public ServletRegistrationBean helloSevlet() {
//        //参数1：自定义Servlet，  HelloServlet
//        // 参数2：url映射
//        ServletRegistrationBean<HelloServlet> bean
//                = new ServletRegistrationBean<>(new HelloServlet(), "/hello");
//        //设置servlet组件参数配置，如下面加载顺序
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
}
