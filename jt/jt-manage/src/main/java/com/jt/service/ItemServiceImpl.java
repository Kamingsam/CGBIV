package com.jt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectPage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.annotation.ShowTest;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	//private QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
	//private UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();

	/**
	 * sql:
	 * 		select * from tb_item limit 起始位置,查询记录数     每页20条
	 * 	第一页:
	 * 		select * from tb_item limit 0,20;   index:0-19 第21条没有取
	 *      第二页:
	 *      select * from tb_item limit 20,20;   
	 *      第三页  
	 *      select * from tb_item limit 40,20;
	 *      第N页
	 *      select * from tb_item limit (page-1)rows,rows;
	 */
	
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		EasyUITable easyUITable = new EasyUITable();
		//方法一：手写分页		
//		Integer total = itemMapper.selectCount(null);//查询总记录数
//		int start =(page-1)*rows;//定义起始位置
//		List<Item> list = itemMapper.findItemByPage(start,rows);
//		
//		easyUITable.setTotal(total);
//		easyUITable.setRows(list);
//		
//		return easyUITable;
		
		
		
		//方法二：使用MP进行分页
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		
		IPage<Item> iPage = new Page<>(page, rows);
		IPage<Item> resultPage = itemMapper.selectPage(iPage, queryWrapper);
		
//		//测试一下
//		List<Item> records = resultPage.getRecords();
//		for (Item item : records) {
//			System.out.println(item.getCreated());
//		}
		
		easyUITable.setTotal((int)(resultPage.getTotal()));
		easyUITable.setRows(resultPage.getRecords());
		
		return easyUITable;

	
	}

//	@Override
//	public void saveItem(Item item) {
//		item.setStatus(1);
//		item.setCreated(new Date());
//		item.setUpdated(item.getCreated());
//		itemMapper.insert(item);
//	}
	
	@Override
	@Transactional
	public void saveItem(Item item, ItemDesc itemDesc) {
		//ID主键自增,所以现在没有id,id=null
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insert(item);
		
		//MP操作时,当item入库之后会将主键自动的回显.
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getUpdated());
		
		itemDescMapper.insert(itemDesc);
		
	}

	@Override
	public void updateItem(Item item) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
	}
	@Transactional
	@Override
	public void updateItem(Item item, ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId());
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

//	@Override
//	public void instockItem(Long[] ids) {
//		changeStatus(ids,2);
//	}
//
//	private void changeStatus(Long[] ids, int status) {
//		// TODO Auto-generated method stub
//		ArrayList<Long> idList = new ArrayList<>(ids.length);
//		for (int i = 0; i < ids.length; i++) {
//			idList.add(ids[i]);
//		}
//		Item item = new Item();
//		item.setStatus(status);
//		item.setUpdated(new Date());
//		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
//		updateWrapper.in("id", idList);
//		itemMapper.update(item, updateWrapper);
//		
//	}
//
//	@Override
//	public void reshelfItem(Long[] ids) {
//		changeStatus(ids, 1);
//	}


	@Override
	public void updateStatus(Long[] ids, int status) {
		// 将数组转为集合
//		ArrayList<Long> idList = new ArrayList<>(ids.length);
//		for (int i = 0; i < ids.length; i++) {
//			idList.add(ids[i]);
//		}
		List<Long> idList=Arrays.asList(ids);
		Item item = new Item();
		item.setStatus(status);
		item.setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);
		
	}

	@ShowTest
	@Transactional
	@Override
	public void deleteItem(Long[] ids) {
//		ArrayList<Long> idList = new ArrayList<>(ids.length);
//		for (int i = 0; i < ids.length; i++) {
//			idList.add(ids[i]);
//		}
		//MP方法
//		itemMapper.deleteBatchIds(Arrays.asList(ids));
		
		itemMapper.deleteItem(ids);
		
		itemDescMapper.deleteBatchIds(Arrays.asList(ids));
		
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		Item item = itemMapper.selectById(itemId);
		return item;
	}

	

	

	
	
	
	
	
}
