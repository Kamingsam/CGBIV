package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatService {

	String findItemCatName(Integer itemCatId);

	List<EasyUITree> findItemCatListByParentId(Long parentId);

	
	
}
