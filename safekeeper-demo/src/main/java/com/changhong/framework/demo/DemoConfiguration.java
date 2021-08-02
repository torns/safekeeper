package com.changhong.framework.demo;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DemoConfiguration {
//    //将scondServlet注册到servletRegistrationBean中
//    @Bean
//    public FilterRegistrationBean secondServlet(){
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new FirstFilter());
//        bean.addUrlPatterns("/f2");
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean secondFilter(){
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new SecondFilter());
//        bean.addUrlPatterns("/f1");
//        return bean;
//    }
//
//    @Bean
//    public WebMvcConfig getWebMvcConfig(){
//        return new WebMvcConfig();
//    }
//
//    public static class WebMvcConfig extends WebMvcConfigurerAdapter {
//
//        @Override
//        public void addInterceptors(InterceptorRegistry registry) {
//            registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");//用于添加拦截规则
//        }
//    }
}
