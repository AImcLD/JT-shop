package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocal {
	private static ThreadLocal<User> thread = new ThreadLocal<>();
	
	public static void setUser(User user) {
		thread.set(user);
	}
	
	public static User getUesr() {
		return thread.get();
	}
	
	//需要处理可能的内存泄露
	public static void remove() {
		thread.remove();
	}
}
