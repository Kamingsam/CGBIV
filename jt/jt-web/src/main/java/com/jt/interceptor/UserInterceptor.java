package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;
@Component
public class UserInterceptor implements HandlerInterceptor{

	@Autowired
	private JedisCluster jedisCluster;

	/**

	 * SpringMVC框架单独封装的拦截器API.简化程序调用过程.
	 * 旧配置:web.xml
	 * 拦截器说明:
	 * 一般使用拦截器时主要的目的是为了控制页面请求的跳转.
	 *  1.preHandle  处理器执行之前进行拦截
	 *  2.postHandle 处理器执行完成之后拦截
	 *  3.afterCompletion 全部业务逻辑执行完成之后并且视图渲染之后拦截
	 */


	/**
	 * 控制用户登录
	 * boolean
	 *    true:  表示放行
	 *    false: 表示拦截
	 *
	 * 拦截器业务分析:
	 *    如果用户登陆则放行.
	 *    如果用户未登录则拦截.
	 * 如何判断用户是否登录
	 * 1.检查用户是否有Cookie信息.
	 * 2.检查redis服务器中是否有ticket信息.
	 * 如果上述判断正常,则表示用户已登录,可以放行.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.判断用户是否有cookie.
		String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
		if(!StringUtils.isEmpty(ticket)) {//为空代表没登录
			//2.表示ticket信息不为null 有值
			String userJSON = jedisCluster.get(ticket);
			if(!StringUtils.isEmpty(userJSON)) {
				//redis数据一切正常
				User user = ObjectMapperUtil.toObj(userJSON, User.class);
				//将user对象传递给（controller，service...）,两种方法
				//1.通过request对象传递
				request.setAttribute("JT_USER", user);
				//2.通过ThreadLocal传递
				ThreadLocalUtil.setUser(user);
				return true; //请求放行
			}else {
				//用户有ticket,但是redis中没有改数据.,cookie有问题
				CookieUtil.deleteCookie(response,"JT_TICKET","jt.com", "/");
			}
		}

		//重定向到用户的登陆页面
		response.sendRedirect("/user/login.html");
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//如果是使用ThreadLocal，需要将ThreadLocal清除防止内存泄漏
		ThreadLocalUtil.removeThread();
	}


}
