package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserMapper userMapper;
	@Override
	public boolean checkUser(String param, int type) {
		String column = (type==1)? "username":((type==2)? "phone":"email");
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq(column, param);
		Integer count = userMapper.selectCount(queryWrapper);
		return count==0? false:true;
	}
	

}
