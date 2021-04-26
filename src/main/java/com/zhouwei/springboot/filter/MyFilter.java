package com.zhouwei.springboot.filter;


import javax.servlet.*;
import java.io.IOException;

//自定义过滤器
public class MyFilter  implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("初始化完成");
    }

    @Override
    public void destroy() {
        System.out.println("销毁完成");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter 过滤完成");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
