package com.xiongdwm.framework.support;

import com.xiongdwm.framework.support.transmit.ComposeMethodArgumentResolver;
import com.xiongdwm.framework.support.transmit.WebInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

public class WebAppConfig implements WebMvcConfigurer {
    @Resource
    private WebInterceptor webInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(webInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ComposeMethodArgumentResolver());
    }
}
