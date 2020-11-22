package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service(timeout=3000)	//3秒超时	
public class DubboUserServiceImpl implements DubboUserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;

	//密码加密
	@Override
	public void saveUser(User user) {
		String md5Password=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setEmail(user.getPhone())
			.setPassword(md5Password)
			.setCreated(new Date())
			.setUpdated(user.getCreated());
		userMapper.insert(user);
		
	}

	/**
	 * 完成用户数据的校验
	 * 1.校验用户名和密码是否正确  密码 明文~~~密文
	 * String return ticket秘钥
	 */
	@Override
	public String findUserByUP(User user) {
		//注册和登录的密码加密必须一致.
		String md5Password = 
				DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		//根据对象中不为null的属性充当where条件
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		if(userDB==null) {
			//根据用户名或密码错误
			return null;
		}
		//表示用户数据是有效的.需要进行单点登录操作
		String ticket = UUID.randomUUID().toString();
		//脱敏处理  user用户名 密码 身份证号 家庭地址 户籍信息 电话号码 金额
		userDB.setPassword("123456你信不?");//伪造数据
		String userJSON = ObjectMapperUtil.toJSON(userDB);
		jedisCluster.setex(ticket,7*24*3600,userJSON);
		return ticket;
	}

}
