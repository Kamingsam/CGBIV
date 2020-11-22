package com.jt.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(timeout=3000,check=false)
	private DubboCartService dubboCartService;
	
	
	/**
	 * 实现购物车页面跳转
	 * http://www.jt.com/cart/show.html
	 * @return
	 */
	@RequestMapping("/show")
	public String showCart(Model model) {
		//查询购物车页面信息
		//1.动态获取userId
		Long userId = ThreadLocalUtil.getUser().getId();
		//2.jt-cart后台项目动态获取购物车记录
		List<Cart> cartList=dubboCartService.findCartListByUserId(userId);
		//3.将数据展现到页面中
		model.addAttribute("cartList", cartList);
		
		
		return "cart";
	}
	
	/**
	 * http://www.jt.com/cart/add/562379.html
	 */
	@RequestMapping("add/{itemId}")
	public String saveCart(Cart cart) {
//		Long userId = 7L; //暂时写死7L
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		dubboCartService.saveCart(cart);
		
		return "redirect:/cart/show.html";
	}
	
	/**
	 * http://www.jt.com/cart/update/num/562379/43
	 */
	@RequestMapping("update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		dubboCartService.updateNum(cart);
		return SysResult.success();
	}
	
	
	/**
	 * http://www.jt.com/cart/delete/562379.html
	 */
	@RequestMapping("delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		dubboCartService.deleteNum(cart);
		return "redirect:/cart/show.html";
	}
	
}
