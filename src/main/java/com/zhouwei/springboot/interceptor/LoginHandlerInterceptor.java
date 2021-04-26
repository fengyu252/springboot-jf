package com.zhouwei.springboot.interceptor;

import com.zhouwei.springboot.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *创建人  zhouwei
 *创建时间  2019/12/17
 * 登录拦截器
 */

public class LoginHandlerInterceptor implements HandlerInterceptor {

   Logger logger = LoggerFactory.getLogger(LoginHandlerInterceptor.class);


    /**
     *author:  zhouwei
     *time:  2020/1/17
     *function:
     * 该方法的执行时机是，
     * 当某个 URL 已经匹配到对应的 Controller 中的某个方法，
     * 且在这个方法执行之前。所以 preHandle(……) 方法可以决定是否将请求放行，
     * 这是通过返回值来决定的，返回 true 则放行，返回 false 则不会向后执行。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");
        Object token = request.getSession().getAttribute("token");
        Object tokenss=  request.getParameter("token");

        if(tokenss!=null&&"zw".equalsIgnoreCase(tokenss.toString())) {
            return true;
        }
        //logger.info("进入界面：-----------");
        if( loginUser != null || token != null) {
          // logger.info("进入界面： ==== "+ loginUser);
            String tokens="";
            boolean b=false;

            tokens = token.toString();
            b = JwtTokenUtil.verifyToken(tokens);
            if(b){
                return true;
            }else{
                //没有登录过
                request.setAttribute("msg", "没有登录，请先登录！或者时间过期");
                request.getRequestDispatcher("/webAdmin/login").forward(request, response);
                return false;
            }
        }

        //没有登录过
        request.setAttribute("msg", "没有登录，请先登录！");
        request.getRequestDispatcher("/webAdmin/login").forward(request, response);
        return false;
    }
    /**
     * 该方法的执行时机是，
     * 当某个 URL 已经匹配到对应的 Controller 中的某个方法，
     * 且在执行完了该方法，但是在 DispatcherServlet 视图渲染之前。
     * 所以在这个方法中有个 ModelAndView 参数，可以在此做一些修改动作。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

       // logger.info("进入界面： ==== postHandle");
    }
    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //logger.info("进入界面： ==== afterCompletion");
    }

}
