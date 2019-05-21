package com.jt.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.ItemCart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Reference(timeout=50000,check =false)
	private DubboOrderService orderService;
	
	@Reference(timeout=50000,check =false)
	private DubboCartService cartService;
	
	
	@RequestMapping("/create")
	public String orderCreate(Model model) {
		Long userId = UserThreadLocal.getUesr().getId();
		List<ItemCart> carts = cartService.findCartByUser(userId);
		model.addAttribute("carts",carts);
		return "order-cart";
	}
	
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult orderSubmit(Order order) {
		try {
			order.setUserId(UserThreadLocal.getUesr().getId());
			String orderId = orderService.saveOrder(order);
			if(!StringUtils.isEmpty(orderId)) {
				return SysResult.ok(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	
	@RequestMapping("/success")
	public String orderSuccess(String id,Model model) {
		Order order = orderService.findOrderById(id);
		model.addAttribute(order);
		return "success";
	}
}
