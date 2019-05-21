package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	HttpClientService httpClientService;

	@Override
	public Item findItemById(Long itemId) {
		String itemUrl = "http://manage.jt.com/web/item/findItemById/"+itemId;
		String getItem = httpClientService.doGet(itemUrl);
		return ObjectMapperUtil.toObject(getItem, Item.class);
	}
	
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		System.out.println(itemId);
		String itemUrl = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
		String getItem = httpClientService.doGet(itemUrl);
		System.out.println(getItem);
		return ObjectMapperUtil.toObject(getItem, ItemDesc.class);
	}
}
