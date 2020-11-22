package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService{
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}

	/**
	 * 需求:新增购物车时,首先根据itemId和userId查询购物车数据.
	 *   如果查询的结果为null,表示用户第一次新增该商品,则直接入库.
	 *   如果查询的结果不为null,表示用户之前购买过该商品,则更新商品数量.
	 */
	@Override
	public void saveCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId())
		.eq("user_id", cart.getUserId());
		//数据库记录 包含主键
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB == null) {
			//说明用户第一次购买
			cart.setCreated(new Date())
			.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//说明用户不是第一次购买,只做数量的更新
			int num = cartDB.getNum() + cart.getNum();
//			cartDB.setNum(num).setUpdated(new Date());
//			//			cartMapper.updateById(cartDB);
//			cartMapper.updateCart(cartDB);
			
			//update优化
			Cart newCart = new Cart();
			newCart.setId(cartDB.getId())
				   .setNum(num)
				   .setUpdated(new Date());
			cartMapper.updateById(newCart);
		}
	}

	@Override
	public void updateNum(Cart cart) {
		//根据userId和itemId查询数据库中的购物车商品
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId())
		.eq("user_id", cart.getUserId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB==null) {
			throw new RuntimeException();
		}
		int num = cart.getNum();
		cartDB.setNum(num).setUpdated(new Date());
		Cart cartTemp = new Cart();
		cartTemp.setId(cartDB.getId())
				.setNum(cartDB.getNum())
				.setUpdated(cartDB.getUpdated());
		cartMapper.updateById(cartTemp);
	}

	@Override
	public void deleteNum(Cart cart) {
		//根据userId和itemId查询数据库中的购物车商品
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId())
		.eq("user_id", cart.getUserId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB==null) {
			throw new RuntimeException();
		}
		
		cartMapper.deleteById(cartDB.getId());
		

	}


}
