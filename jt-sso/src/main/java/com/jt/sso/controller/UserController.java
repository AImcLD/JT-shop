package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	JedisCluster jedisCluster;
	@Autowired
	UserService userService;
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable("param") String param,@PathVariable("type") int type,String callback){
		boolean flage = userService.checkUser(param, type);
		SysResult sysResult =SysResult.ok(flage);
		return new JSONPObject(callback, sysResult);
	}
	
	//根据token数据获取用户信息
	@RequestMapping("/query/{token}")
	public JSONPObject findUserByToken(@PathVariable String token,String callback) {
		String userJSON = jedisCluster.get(token);
		if(StringUtils.isEmpty(userJSON)) {
			return null;
		}
		return new JSONPObject(callback, SysResult.ok(userJSON));
	}

}
