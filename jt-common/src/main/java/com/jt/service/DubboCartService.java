package com.jt.service;

import java.util.List;

import com.jt.pojo.ItemCart;

public interface DubboCartService {

	List<ItemCart> findCartByUser(Long userId);

	void saveCart(ItemCart cart);

	void updateCartNum(ItemCart cart);

	void deleteCart(ItemCart cart);

}
