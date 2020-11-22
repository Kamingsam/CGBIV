package com.jt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{

	/**
	 * 高版本中可以省略@Param注解,参数名称必须一致.
	 * @param start
	 * @param rows
	 * @return
	 */
	@Select("select * from tb_item order by updated desc limit #{start},#{rows}")
	List<Item> findItemByPage(Integer start, Integer rows);

	void deleteItem(@Param("ids")Long[] ids);

//	@Select("select name from tb_item_cat where id=#{itemCatId}")
//	String findItemName(Integer itemCatId);


}
