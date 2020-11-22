package com.jt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //注解对谁有效
@Retention(RetentionPolicy.RUNTIME) //运行时有效
public @interface CacheFind {
	String key() default ""; //如果用户设定了参数，则使用的用户的	
							 //如果用户没有设定参数，就使用自动生成
	
	int seconds() default 0; //超时失效时间，默认为0
}
