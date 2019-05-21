package com.jt.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service
public class RedisService {
	@Autowired(required = false)
	private JedisSentinelPool pool;
	
	public void setex(String key,Integer seconds,String value) {
		Jedis jedis = pool.getResource();
		jedis.setex(key, seconds, value);
		jedis.close();
	}
	
	public String get(String key) {
		Jedis jedis = pool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}
	
}
