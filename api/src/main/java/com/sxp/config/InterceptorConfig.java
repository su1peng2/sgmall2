package com.sxp.config;

import com.sxp.interceptor.SetTimeInterceptor;
import com.sxp.interceptor.WebInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @author 粟小蓬
 * 添加拦截器
 * 配置拦截路径
 * 配置不拦截路径
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private WebInterceptor interceptor;

    @Autowired
    private SetTimeInterceptor setTimeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(interceptor);
        interceptorRegistration.addPathPatterns("/shopCart/**");
        interceptorRegistration.addPathPatterns("/address/**");
        interceptorRegistration.addPathPatterns("/order/**");
        interceptorRegistration.addPathPatterns("/user/check");

        InterceptorRegistration interceptorRegistration1 = registry.addInterceptor(setTimeInterceptor);
        interceptorRegistration1.addPathPatterns("/**");
    }
}
