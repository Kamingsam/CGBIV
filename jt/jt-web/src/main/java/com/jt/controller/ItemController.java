package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.WebItemService;

@Controller
@RequestMapping("/items/")
public class ItemController {
	@Autowired
	private WebItemService webItemService;
	/**
	 * 
	 *    用户前台动态获取商品信息.
	 *  1.url:http://www.jt.com/items/562379.html
	 *  2.用户接收url请求之后,动态获取商品信息(item+itemDesc)
	 *  3.获取数据之后,在item.jsp页面中进行展现.
	 *  4.取值方式: ${item.title},${itemDesc.itemDesc }
	 */
	@RequestMapping("{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		Item item = webItemService.findItemById(itemId);
		ItemDesc itemDesc = webItemService.findItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
