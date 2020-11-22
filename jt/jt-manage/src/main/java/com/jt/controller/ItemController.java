package com.jt.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/item/")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 关于SpringMVC页面取值复习.
	 * 1.页面标签
	 * 	 <input  name="page" value="1"  />
	 * 	 <input  name="rows" value="20"  />  提交request对象
	 * 2.SpringMVC底层DispatcherServlet.底层实现依然采用servlet的方式动态的获取
	 * 	 数据.可以利用request对象的方式获取数据.
	 * 	request.getParameter("xxx");
	 *  传参说明: 用户传递参数之后,服务器取数据时,按照指定的要求获取数据.如果key不同,
	 *  那么获取到的数据为null.而不报错.
	 *  规则:SpringMVC中参数的名字,必然和页面中的name属性相同.
	 *  
	 * 3.SpringMVC将上述操作简化. 并且可以自动的实现数据类型的切换
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("query")
	@ResponseBody
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		EasyUITable easyVo = itemService.findItemByPage(page, rows);
		return easyVo;
	}

//	@RequestMapping("save")
//	@ResponseBody
//	public SysResult saveItem(Item item) {
////		try {
////			itemService.saveItem(item);
////			return SysResult.success();
////		} catch (Exception e) {
////			return SysResult.fail();
////		}
//		itemService.saveItem(item);
//		return SysResult.success();
//	}
	
	
	@RequestMapping("save")
	@ResponseBody
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
//		try {
//			itemService.saveItem(item);
//			return SysResult.success();
//		} catch (Exception e) {
//			return SysResult.fail();
//		}
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}
	
	
	/**
	 * url: /item/update
	 *   参数:  页面表格参数
	 *   返回值: SysResult对象
	 * @return
	 */
//	@RequestMapping("update")
//	@ResponseBody
//	public SysResult updateItem(Item item) {
//
//		itemService.updateItem(item);
//		return SysResult.success();
//	}
	
	@RequestMapping("update")
	@ResponseBody
	public SysResult updateItem(Item item,ItemDesc itemDesc) {

		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * 通过上架/下架的操作,实现了商品状态的修改.
	 * 上架: item属性status=1
	 * 下架: item属性 status=2 
	 * @param ids
	 * @return
	 */
	@RequestMapping("instock")
	@ResponseBody
	public SysResult instockItem(Long[] ids) {
//		System.out.println(Arrays.toString(ids));
//		itemService.instockItem(ids);
		int status=2;
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	@RequestMapping("reshelf")
	@ResponseBody
	public SysResult reshelfItem(Long[] ids) {
		//System.out.println(Arrays.toString(ids));
		int status=1;
		itemService.updateStatus(ids,status);
//		itemService.reshelfItem(ids);
		return SysResult.success();
	}
	
	
	@RequestMapping("delete")
	@ResponseBody
	public SysResult deleteItem(Long[] ids) {
		itemService.deleteItem(ids);
		return SysResult.success();
	}
	

	/**
	 * url: /item/query/item/desc/1474392154
	 * 参数:采用restFul方式实现参数的传递
	 * 返回值: SysResult对象
	 * 业务: 根据itemId查询商品详情.
	 * @return
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable Long itemId) {
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}


}
