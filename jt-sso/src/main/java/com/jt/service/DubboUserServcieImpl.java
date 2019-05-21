package com.jt.service;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service
public class DubboUserServcieImpl implements DubboUserServcie{
	@Autowired
	private UserMapper mapper;
	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public void saveUser(User user) {
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setCreated(new Date()).setUpdated(new Date());
		user.setPassword(md5Pass);
		mapper.insert(user);
	}

	
	@Override
	public String doLogin(User user) {
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//不为空的字段作为where的条件
		QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
		User check = mapper.selectOne(queryWrapper);
		if(check==null)
			throw new RuntimeException();
		String token = user.getUsername()+user.getPassword();
		String md5Token = DigestUtils.md5DigestAsHex(token.getBytes());
		System.out.println(jedisCluster.getClusterNodes());
		check.setPassword("123321");
		jedisCluster.setex(md5Token, 7*24*3600, ObjectMapperUtil.toJSON(check));
		return md5Token;
	}
}









