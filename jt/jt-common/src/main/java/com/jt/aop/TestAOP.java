package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.jt.annotation.ShowTest;

@Aspect
@Component
public class TestAOP {
	@Around("execution(* com.jt.service..*.*(..))")
	public Object testAop(ProceedingJoinPoint jp) {
		System.out.println("before");
		try {
			Object obj = jp.proceed();
			System.out.println("after");
			return obj;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Around("@annotation(testB)")
	public Object testAOP2(ProceedingJoinPoint jp,ShowTest testB) {
		System.out.println("b1");
		try {
			Object obj = jp.proceed();
			System.out.println("b2");
			return obj;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
