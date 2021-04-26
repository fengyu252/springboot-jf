package com.zhouwei.springboot.interceptor;

import com.zhouwei.springboot.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(LoginHandlerInterceptor.class);

    public LoginHandlerInterceptorAdapter() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");
        Object token = request.getSession().getAttribute("token");
        logger.info("进入界面：*******");
        if( loginUser != null || token != null) {
            logger.info("进入界面：****** "+ loginUser);
            String tokens="";
            boolean b=false;
            tokens = token.toString();
            b = JwtTokenUtil.verifyToken(tokens);
            if(b){
                return true;
            }else{
                //没有登录过
                request.setAttribute("msg", "没有登录，请先登录！");
                request.getRequestDispatcher("/webAdmin/login").forward(request, response);
                return false;
            }
        }

        //没有登录过
        request.setAttribute("msg", "没有登录，请先登录！");
        request.getRequestDispatcher("/webAdmin/login").forward(request, response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("进入界面：******* postHandle");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("进入界面：******* afterCompletion");
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("进入界面：******* afterConcurrentHandlingStarted");
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
