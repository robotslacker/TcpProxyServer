package com.robotslacker.tcpproxy.config;

import com.robotslacker.tcpproxy.interceptor.LanguageInterceptor;
import com.robotslacker.tcpproxy.interceptor.ServiceStatusInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
            registry.
                addInterceptor(new ServiceStatusInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/shutdown")
                .excludePathPatterns("/restart");
            registry.
                addInterceptor(new LanguageInterceptor())
                .addPathPatterns("/**");
        }
}
