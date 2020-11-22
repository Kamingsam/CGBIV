package com.jt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jt.interceptor.UserInterceptor;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	
	@Autowired
	private UserInterceptor userInterceptor;
	
	//开启匹配后缀型配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		
		configurer.setUseSuffixPatternMatch(true);//设置是否是后缀模式匹配，如“/user”是否匹配/user.*，默认真即匹配；
	}
	//添加拦截器
	//添加拦截器配置
	//  /cart/** 所有的路径        /cart/* 一级目录
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInterceptor).addPathPatterns("/cart/**","/order/**");
//		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
