package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;

	//@CacheFind
	@Override
	public String findItemCatName(Integer itemCatId) {
		//查询商品分类对象
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		//获取商品分类名称
		return itemCat.getName();
	}

	/*查询数据库记录,之后实现商品分类展现
	 * 业务分析:
	 * 	1.要求返回List<EasyUITree> 数据.
	 * 	2.需要将itemCatList集合转化为List<VO>对象
	 * 	2.itemCatMapper.selectList(); 查询的是数据库itemCat数据.
	 * */
	@CacheFind
	@Override
	public List<EasyUITree> findItemCatListByParentId(Long parentId) {
//		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("parent_id", parentId);
//		List<ItemCat> selectList = itemCatMapper.selectList(queryWrapper);
//		List<EasyUITree> eList  = new ArrayList<>();
//		for (ItemCat itemCat : selectList) {
//			Long id = itemCat.getId();
//			String text = itemCat.getName();
//			//如果是父级 默认closed,否则开启open
//			String state = itemCat.getIsParent()?"closed":"open";
//			EasyUITree eTree = new EasyUITree(id, text, state);
//			eList.add(eTree);
//		}
//		return eList;
		
		//1.根据parentID查询数据库记录.
		List<ItemCat> itemCatList = findItemCatList(parentId);
		
		//2.itemCatList~~~~List<EasyUITree>
		List<EasyUITree> treeList = new ArrayList<>(itemCatList.size());
		//问：为什么要设置list集合的长度?
		//答：控制扩容的次数，节约空间避免它无限度的开辟空间，

		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			//如果是父级 默认closed,否则开启open
			String state = itemCat.getIsParent()?"closed":"open";
			EasyUITree eTree = new EasyUITree(id, text, state);
			treeList.add(eTree);
		}
		return treeList;
		
	}

	//根据parentId查询分类信息
	private List<ItemCat> findItemCatList(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		return itemCatList;
	}

	

	
	
	
	
	
	
	
}
