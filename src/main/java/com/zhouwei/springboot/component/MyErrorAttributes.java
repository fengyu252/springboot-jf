package com.zhouwei.springboot.component;



import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
/**
 *author:  zhouwei
 *time:  2020/1/19
 *function: 添加错误配置类
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    /**
     * 自定义数据进行响应
     * @param webRequest
     * @param includeStackTrace
     * @return
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, includeStackTrace);
        map.put("company", "zhouwei");
       // map.put("requests",webRequest.get)
        return map;
    }
}
