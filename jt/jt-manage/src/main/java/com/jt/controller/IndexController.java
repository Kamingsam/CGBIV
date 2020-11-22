package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	/**
	 * 动态获取url中的参数地址
	 * restful风格1：
	 * 1.参数使用"/"分割
	 * 2.参数使用{}包裹
	 * 3.需要动态获取参数，需要配合注解@PathVariable使用
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
	}
	
	
	/**
	 *  restFul风格2:
	 *     问题:能否简化用户的url请求个数?
	 *     案例:
	 *     1.url:/user/addUser			post类型
	 *     2.url:/user/updateUser		put类型
	 *     3.url:/user/findUser  		get类型
	 *     4.url:/user/deleteUser		delete类型
	 *  解决方案:
	 *  	利用不同的请求类型,实现用户的CRUD操作.
	 *  	url:/user  type="get" 查询
	 *  	url:/user  type="post" 新增
	 */
	/*//@RequestMapping(value="/user",method=RequestMethod.GET)
	@GetMapping("/user")
	public Item getItemById() {
		
		System.out.println("数据查询");
		return null;
	}
	
	@RequestMapping(value="/user",method=RequestMethod.POST)
	//@PostMapping("/user")
	public Item insertUser(XXX XXX) {
		
		System.out.println("数据查询");
		return null;
	}*/
}
