package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;
@Component
public class UserInterceptor implements HandlerInterceptor{
	@Autowired
	JedisCluster jedisCluster;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Cookie[] cookies = request.getCookies();
		String name = "JT_TICKET";
		//一下逻辑与老师不同
		if(cookies.length!=0) {
			for (Cookie cookie : cookies) {
				if(name.equals(cookie.getName())) {
					 String userJSON = jedisCluster.get(cookie.getValue());
					User  user = ObjectMapperUtil.toObject(userJSON, User.class);
					/*//购物车获取id的方式一
					request.setAttribute("cart_user", user);*/
					//购物车获取方式二
					UserThreadLocal.setUser(user);
					return  jedisCluster.exists(cookie.getValue());
				}
			}
		}
		response.sendRedirect("http://www.jt.com/user/login.html");
		return false;
	}

	
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	
	
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove();
	}
}








