package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;

	@RequestMapping("/ssoTest")
	public String ssoTest() {
		return "sso服务器测试成功";
	}

	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
			@PathVariable Integer type,
			String callback) {
		boolean flag = userService.checkUser(param,type);
		SysResult result = SysResult.success(flag);
		JSONPObject jsonpObject = new JSONPObject(callback, result);
//		try {
//			//业务调用返回true/false
//			boolean flag = userService.checkUser(param,type);
//			SysResult result = SysResult.success(flag);
//			jsonpObject = new JSONPObject(callback, result);			
//		} catch (Exception e) {
//			e.printStackTrace();
//			SysResult result = SysResult.fail();
//			jsonpObject = new JSONPObject(callback, result);
//		}
		return jsonpObject;
	}
	/**
	 * 业务需求:完成用户信息的跨域获取
	 * url:http://sso.jt.com/user/query/58b7a85f-5f81-4162-9515-28aa8ef8971a?callback=jsonp1582852795539&_=1582852795697
	 * 参数:/{ticket},callback信息
	 * 返回值结果: sysResult对象
	 * 
	 * 业务说明:
	 * 	 1.根据ticket信息,可以动态获取redis中用户信息.
	 * 	 2.如果ticket有数据,则获取的是userJSON数据.
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject queryName(@PathVariable String ticket,String callback,HttpServletResponse response) {
		String userJSON = jedisCluster.get(ticket);
		SysResult result = null;
		if (StringUtils.isEmpty(userJSON)) {
			//缓存中没有数据.证明ticket信息有问题,cookie有误.应该删除cookie
			//如何删除cookie 设定有效时间为0
//			Cookie cookie = new Cookie("JT_TICKET","");
//			cookie.setDomain("jt.com");
//			cookie.setPath("/");
//			cookie.setMaxAge(0);
//			response.addCookie(cookie);
			CookieUtil.deleteCookie(response, "JT_TICKET", "jt.com", "/");
			result = SysResult.fail();
		}else {			
			result = SysResult.success(userJSON);
		}
		JSONPObject jsonpObject = new JSONPObject(callback, result);
		return jsonpObject;
	}
}
