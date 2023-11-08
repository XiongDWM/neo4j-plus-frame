package com.xiongdwm.framework.support.transmit;

import com.xiongdwm.framework.relational.resources.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userService;
    @Value("${spring.profiles.active}")
    private String active="dev";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if("dev".equals(active)){
            return true;
        }
        //todo 从user中获取token
        String token = request.getHeader("token");
        return token != null && !"".equals(token);
    }
}
