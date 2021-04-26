package com.zhouwei.springboot.config;

import com.zhouwei.springboot.interceptor.LoginHandlerInterceptor;
import com.zhouwei.springboot.interceptor.LoginHandlerInterceptorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 *创建人  zhouwei
 *创建时间  2019/12/17
 * 配置项目主页面
 */
@Configuration
public class MySpringMvcConfiurer {

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
       return  new WebMvcConfigurer(){
           @Override
           public void addViewControllers(ViewControllerRegistry registry) {
               registry.addViewController("/").setViewName("login");
               //registry.addViewController("/index.html").setViewName("html/login_soft");
               registry.addViewController("/login.html").setViewName("goLogin");
               registry.addViewController("/login.html").setViewName("login");

           }
           @Override
           public void addInterceptors(InterceptorRegistry registry) {
               registry.addInterceptor(new LoginHandlerInterceptor())
                       //指定要拦截的请求 /** 表示拦截所有请求
                      // .addPathPatterns("/sjmobile/**")
                       .addPathPatterns("/webAdmin/**")
                       //排除不需要拦截的请求路径
                       .excludePathPatterns("/sjmobile/**","/spider/ExcelDownload",
                               "/webAdmin/loginAdmin","/webAdmin/login",
                               "/webAdmin/goLogin","/excelDownload","/ExcelDownload")
                       //springboot2+之后需要将静态资源文件的访问路径 也排除
                       .excludePathPatterns("/css/*", "/img/*","/js/*","/media/**");
           }
       };
    }
}
