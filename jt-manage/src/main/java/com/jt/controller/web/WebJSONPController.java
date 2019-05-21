package com.jt.controller.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;

@RestController
public class WebJSONPController {
	
	@RequestMapping("web/testJSONP")
	public JSONPObject TestJSONP(String callback) {
		User user = new User();
		user.setId(10001);
		user.setName("tomcat");
		//String userJSON = ObjectMapperUtil.toJSON(callback);
		return new JSONPObject(callback, user)/*callback+"("+")"*/;
	}
}
