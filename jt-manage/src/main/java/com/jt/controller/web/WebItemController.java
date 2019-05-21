package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item/")
public class WebItemController {
	@Autowired
	ItemService itemService;
	@RequestMapping("findItemById/{itemId}")
	public Item findItemByItemId(@PathVariable Long itemId) {
		return itemService.findItemById(itemId);
	}
	
	@RequestMapping("findItemDescById/{itemId}")
	public ItemDesc findItemDescByItemId(@PathVariable Long itemId) {
		return itemService.findItemDescById(itemId);
	}
}
