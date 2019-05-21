package com.jt.serviceimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.service.DubboOrderService;

@Service(timeout=3000)
public class DubboOrderServiceImpl implements DubboOrderService{
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	@Autowired
	private OrderItemMapper itemMapper;
	
	@Transactional
	@Override
	public String saveOrder(Order order) {
		String totalPrice = totalPrice(order);
		String orderId = ""+ order.getUserId()+System.currentTimeMillis();
		Date date = new Date();
		order.setOrderId(orderId).setPayment(totalPrice)
		.setStatus(1).setCreated(date).setUpdated(date);
		orderMapper.insert(order);
		
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId).setCreated(date).setUpdated(date);
		orderShippingMapper.insert(shipping);
		
		List<OrderItem> lists = order.getOrderItems();
		for (OrderItem orderItem : lists) {
			orderItem.setOrderId(orderId)
			.setCreated(date)
			.setUpdated(date);
			itemMapper.insert(orderItem);
		}
		
		return orderId;
	}

	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping orderShipping = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<OrderItem>();
		queryWrapper.eq("order_id", id);
		List<OrderItem>  items = itemMapper.selectList(queryWrapper);
		order.setOrderItems(items)
		.setOrderShipping(orderShipping);
		return order;
	}
	
	//计算订单的总价
	public String totalPrice(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		Long totalPrice = 0L;
		for (OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getPrice() * orderItem.getNum();
		}
		return totalPrice.toString();
	}
}




