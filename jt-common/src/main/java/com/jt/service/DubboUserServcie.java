package com.jt.service;

import com.jt.pojo.User;

public interface DubboUserServcie {
	public void saveUser(User user);

	public String doLogin(User user);
}
