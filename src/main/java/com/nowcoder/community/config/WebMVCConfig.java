package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import com.nowcoder.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.png", "/img/*.jpg", "/img/*.jepg")
                .addPathPatterns("/register", "/login");  // /**表示static下的所有目录

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.png", "/img/*.jpg", "/img/*.jepg");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.png", "/img/*.jpg", "/img/*.jepg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.png", "/img/*.jpg", "/img/*.jepg");
    }
}
