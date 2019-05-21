package com.jt.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.DubboUserServcie;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Reference(timeout=3000,check=false)
	private DubboUserServcie userService;
	@Autowired
	private JedisCluster jedisCluster;

	@RequestMapping("/{moduleName}")
	public String login(@PathVariable String moduleName) {
		return moduleName;
	}

	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult register(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		}catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	//实现用户登录
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user,HttpServletResponse response) {
		try {
			String token = userService.doLogin(user);
			if(!StringUtils.isEmpty(token)) {
				//cookie的名字要一致
				Cookie cookie = new Cookie("JT_TICKET",token);
				cookie.setMaxAge(7*24*3600);
				cookie.setDomain("jt.com");
				//代表Cookie的权限
				cookie.setPath("/");
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		//数组判断是否为空用length
		String name = "JT_TICKET";
		if(cookies.length!=0) {
			for (Cookie cookie : cookies) {
				if(name.equals(cookie.getName())) {
					jedisCluster.del(cookie.getValue());
				}
				break;
			}
			}
		Cookie cookie = new  Cookie(name, "");
		//删除cookie用覆盖
		cookie.setMaxAge(0);
		cookie.setDomain("jt.com");
		cookie.setPath("/");
		response.addCookie(cookie);
		return "redirect:/";
	}
}
