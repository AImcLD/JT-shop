package com.jt.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.ItemCart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(timeout=3000)
	private DubboCartService dubboCartService;
	
	//加载购物车
	@RequestMapping("/show")
	public String showCartByUser(Model model){
		//Long userId = 7L;
		/*//方式一
		Long userId =((User) httpServletRequest.getAttribute("cart_user")).getId();*/
		Long userId = UserThreadLocal.getUesr().getId();
		List<ItemCart> cartList = dubboCartService.findCartByUser(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("add/{itemId}")
	public String saveCart(ItemCart cart,HttpServletRequest httpServletRequest) {
		Long userId = UserThreadLocal.getUesr().getId();
		cart.setUserId(userId);
		dubboCartService.saveCart(cart);
		//这里使用从定向，用转发需要重复写代码
		return "redirect:/cart/show";
	}
	
	@RequestMapping("/update/num/{itemId}/{num}")
	public SysResult updateCartNum(ItemCart cart) {
		try {
			Long userId = UserThreadLocal.getUesr().getId();
			cart.setUserId(userId);
			dubboCartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(ItemCart cart) {
		try {
			Long userId = UserThreadLocal.getUesr().getId();
			cart.setUserId(userId);
			dubboCartService.deleteCart(cart);
			return "redirect:/cart/show";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
















