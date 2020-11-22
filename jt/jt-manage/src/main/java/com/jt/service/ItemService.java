package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {
	EasyUITable findItemByPage(Integer page,Integer rows);

//	void saveItem(Item item);

	void updateItem(Item item);

//	void instockItem(Long[] ids);
//
//	void reshelfItem(Long[] ids);

	void deleteItem(Long[] ids);

	void updateStatus(Long[] ids, int status);

	void saveItem(Item item, ItemDesc itemDesc);

	ItemDesc findItemDescById(Long itemId);

	void updateItem(Item item, ItemDesc itemDesc);

	Item findItemById(Long itemId);


//	String findItemName(Integer itemCatId);
}
