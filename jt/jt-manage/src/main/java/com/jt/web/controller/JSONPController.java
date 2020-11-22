package com.jt.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class JSONPController {
//	@RequestMapping("/web/testJSONP")
//	public String jsonp(String callback) {
//		ItemDesc itemDesc = new ItemDesc(1000L, "商品详情信息");
//		String json = ObjectMapperUtil.toJSON(itemDesc);
//		return callback+"("+json+")";
//	}

	/**
	 * JSONPObject是跨域访问的API
	 * function 是回调函数的名字
	 * value 是需要返回的对象
	 */
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp02(String callback) {
		ItemDesc itemDesc = new ItemDesc(1000L, "商品详情信息");
		return new JSONPObject(callback, itemDesc);
	}
}
