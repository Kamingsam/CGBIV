package com.jt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.annotation.CacheFind;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Aspect
@Component
public class CacheAOP {
	//切面 = 切入点表达式 + 通知
	//需求1:前置通知 拦截所有的service层的方法.
	//定义公共的切入点表达式
	//	@Pointcut("execution(* com.jt.service..*.*(..))")
	//	public void pointcut() {}

	//获取目标对象的方法名称.
	//通知关联切入点表达式4
	//@Before("pointcut")
	//	@Before("execution(* com.jt.service..*.*(..))")
	//	public void before(JoinPoint jp) {
	//		String targetClassName = jp.getSignature().getDeclaringTypeName();
	//		String methodName = jp.getSignature().getName();
	//		System.out.println("目标类名"+targetClassName);
	//		System.out.println("目标方法名"+methodName);
	//	}
	
	//@Autowired
	//private Jedis jedis;//单台redis
	//@Autowired
	//private ShardedJedis jedis;//redis分片
//	@Autowired
//	@Qualifier("sentinelJedis")//指定bean的名称进行注入
//	private Jedis jedis;
	
	@Autowired
	private JedisCluster jedis;

	/**
	 * 1.添加环绕通知  使用环绕通知必须添加返回值
	 * 2.拦截自定义注解
	 *   需求: 动态获取自定义注解中的参数
	 * 工作原理说明:
	 * 	定义注解的变量名称  cacheFind
	 *      通知参数接收名称     cacheFind
	 *      除了匹配名称之外,还需要匹配类型.
	 *      
	 * 注意事项: 
	 * 	1.环绕通知使用时,必须添加ProceedingJoinPoint
	 *  2.并且其中的参数joinPoint,必须位于第一位.
	 *  
	 * 缓存实现业务思路:
	 * 	1.准备key   1:动态的生成key    2:用户指定的key    key是否有值.
	 * 	2.先查询缓存
	 * 		2.1 没有数据   执行数据库操作. 执行目标方法
	 * 		将目标方法的返回值 转化为JSON串,保存到redis中.
	 * 		2.2 有数据      动态获取缓存数据之后利用工具API转化为真实的对象.
	 */
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint jp,CacheFind cacheFind) {
		System.out.println("调用成功");
		String key = getKey(jp,cacheFind);
		//从缓存（redis）根据key获取value
		String value = jedis.get(key);
		//定义目标方法返回对象
		Object object=null;
		//判断value是否为空
		try {
			if(StringUtils.isEmpty(value)) {
				//value为空，执行目标方法，查询数据库
				object = jp.proceed();
				//将数据库查询到的，存入缓存
				String json = ObjectMapperUtil.toJSON(object);
				if (cacheFind.seconds()>0) {//需要设置超时失效时间
					int seconds = cacheFind.seconds();
					jedis.setex(key, seconds, json);
				}else {
					jedis.set(key, json);
				}
				System.out.println("AOP查询数据库");
			}else {//value不为空，从缓存获取
				//将value转为java对象
				//通过MethodSignature接口获取目标方法返回值类型
				MethodSignature ms = (MethodSignature) jp.getSignature();
				Class<?> cls = ms.getReturnType();
				object = ObjectMapperUtil.toObj(value, cls);
				System.out.println("AOP查询缓存");
//				System.out.println(jedis.info());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return object;
	}
	//动态获取key
	private String getKey(ProceedingJoinPoint jp, CacheFind cacheFind) {
		//检查用户是否传递key
		String key = cacheFind.key();
		if(StringUtils.isEmpty(key)) {
			//1.获取类名
			String className = jp.getSignature().getDeclaringTypeName();
			//2.获取方法名
			String methodName = jp.getSignature().getName();
			//3.获取第一个参数的值
			Object arg = jp.getArgs()[0];
			//包名.类名.方法名::第一个参数
			key = className+"."+methodName+"::"+arg;
		}
		return key;
	}



}
