package com.jt.aop;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jt.config.RedisConfig;
import com.jt.service.RedisService;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
@Aspect
@Service
public class RedisAspect {
	@Autowired
	JedisCluster jedisCluster;
/*	@Autowired
	RedisService redisService;*/
	/*	@Autowired
	ShardedJedis jedis;*/

	@Around("@annotation(com.jt.ann.Cache)")
	public Object redisAspect(ProceedingJoinPoint pj)throws Throwable{

		MethodSignature signature = (MethodSignature) pj.getSignature();
		//Object targetClass = pj.getTarget();
		Object[] paramObjects = pj.getArgs();

		//String methodname = signature.getMethod().getName();
		//Class<?>[] parameterTypes = signature.getParameterTypes();

		//Method method = targetClass.getClass().getMethod(methodname, parameterTypes);
		String pid = paramObjects[0].toString();
		String key = "ITEM_CAT_"+pid;
		List<Object> treeList = new ArrayList<>();
		 /*//使用连接池时使用
		 String jsonResult = redisService.get(key);*/
	/*	 //使用直连是使用
		  String jsonResult = jedis.get(key);*/
		//使用集群
		String jsonResult = jedisCluster.get(key);
		 if(StringUtils.isEmpty(jsonResult)) {
			 treeList =  (List<Object>) pj.proceed();
			 //将list集合转化为json串    对象转化为json必然调用get方法
			 String json = ObjectMapperUtil.toJSON(treeList);
			 //赋值操作并且添加超时时间

/*			 // 使用连接池时使用
			 redisService.setex(key,3600*24*7,json);	*/
			 //使用集群
			 jedisCluster.setex(key, 3600*24*7, json);
			 /*
			  * 
			  * jedis.setex(key, 3600*24*7, json);*/		
			 System.out.println("查询数据库~!!!!!!!!");
		 }else {
			 treeList = ObjectMapperUtil.toObject(jsonResult, treeList.getClass());
			 System.out.println("查询redis缓存!!!!!!");
		 }
		 return treeList;

	}
}







