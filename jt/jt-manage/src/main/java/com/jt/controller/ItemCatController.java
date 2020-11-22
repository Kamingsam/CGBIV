package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
@RestController
@RequestMapping("/item/cat/")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	
	/**
	 * 根据id查询商品分类名称 
	 * url:/item/cat/queryItemName
	 *   参数: itemCatId:val
	 *   结果: 商品分类名称
	 */
	@RequestMapping("queryItemName")
	@ResponseBody
	public String queryItemName(Integer itemCatId) {
		String itemCatName = itemCatService.findItemCatName(itemCatId);
		return itemCatName;
	}
	
	/**
	 * 实现商品列表分类展现
	 * 1.url请求地址 item/cat/list
	 * 2.结果集 List<EasyUITree> [{},{},{}]
	 * 3.参数: id="商品分类的ID"
	 *   需求:查询全部的一级商品分类目录
	 * @RequestParam 作用:动态的获取请求的参数.
	 * 		value() 
	 * 		name()
	 * 		required() 是否为必传参数,默认值为 true
	 * 		defaultValue() 设定默认值.
	 */
	@RequestMapping("list")
	public List<EasyUITree> findTreeByParentId(@RequestParam(value = "id",defaultValue = "0")Long parentId){
//		if (id==null) {
//			id=0L;
//		}
//		Long parentId = id;//指定父级分类id,0L为一级
		
		return itemCatService.findItemCatListByParentId(parentId);
	}
	
	
}
