package com.jt.serviceimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.ItemCart;
import com.jt.service.DubboCartService;

@Service(timeout=3000)
public class DubboCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<ItemCart> findCartByUser(Long userId) {
		QueryWrapper<ItemCart> queryWrapper = new QueryWrapper<ItemCart>();
		queryWrapper.eq("user_id", userId);
		List<ItemCart> cartList = cartMapper.selectList(queryWrapper);
		return cartList;
	}
	//购物车入库
	@Override
	public void saveCart(ItemCart cart) {
		QueryWrapper<ItemCart> queryWrapper = new QueryWrapper<ItemCart>();
		queryWrapper.eq("user_id", cart.getUserId());
		ItemCart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB==null) {
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
			System.out.println("执行添加");
		}else {
			cartDB.setNum(cartDB.getNum()+cart.getNum());
			cartDB.setUpdated(new Date());
			cartMapper.updateById(cartDB);
			System.out.println("执行更改");
		}
	}
	
	//修改购物车商品数量
	@Override
	public void updateCartNum(ItemCart cart) {
		ItemCart cartDB = new ItemCart();
		cartDB.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<ItemCart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("item_id", cart.getItemId()).eq("user_id", cart.getUserId());
		cartMapper.update(cart, updateWrapper);
	}
	@Override
	public void deleteCart(ItemCart cart) {
		ItemCart cartDB = new ItemCart();
		cartDB.setUserId(cart.getUserId()).setItemId(cart.getItemId());
		QueryWrapper<ItemCart> queryWrapper = new QueryWrapper<ItemCart>();
		queryWrapper.eq("user_id", cartDB.getUserId()).eq("item_id", cartDB.getItemId());
		cartMapper.delete(queryWrapper);
	}
	
}




