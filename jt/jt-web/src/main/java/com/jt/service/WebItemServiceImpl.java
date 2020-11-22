package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.annotation.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class WebItemServiceImpl implements WebItemService {
	
	@Autowired
	private HttpClientService httpClientService;

	/**
	 * http://manage.jt.com/web/item/findItemById?itemId=562379
	 */
	@CacheFind
	@Override
	public Item findItemById(Long itemId) {
		String url ="http://manage.jt.com/web/item/findItemById";
		Map<String, String> params = new HashMap<>();
		params.put("itemId", itemId+"");
		String res = httpClientService.doGet(url, params);
		//将json串转换为对象
		Item item = ObjectMapperUtil.toObj(res, Item.class);
		return item;
	}
	@CacheFind
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url ="http://manage.jt.com/web/item/findItemDescById";
		Map<String, String> params = new HashMap<>();
		params.put("itemId", itemId+"");
		String res = httpClientService.doGet(url, params);
		//将json串转换为对象
		ItemDesc itemDesc=ObjectMapperUtil.toObj(res, ItemDesc.class);
		return itemDesc;
	}

}
