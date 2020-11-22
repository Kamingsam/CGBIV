package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {

	//动态获取当前端口号  --从配置文件中获取
	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("/getMsg")
	public String getMsg() {
		return "当前服务器的端口号为"+port;
	}
}
