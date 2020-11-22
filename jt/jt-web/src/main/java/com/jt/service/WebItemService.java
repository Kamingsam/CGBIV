package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public interface WebItemService {

	Item findItemById(Long itemId);

	ItemDesc findItemDescById(Long itemId);

}
