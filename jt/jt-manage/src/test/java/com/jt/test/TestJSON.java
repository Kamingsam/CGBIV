package com.jt.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestJSON {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Test
	public void test2JSON() throws JsonProcessingException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L);
		itemDesc.setItemDesc("JSON测试");
		
		String itemDescJson = MAPPER.writeValueAsString(itemDesc);
		System.out.println(itemDescJson);
		
		ItemDesc itemDesc2 = MAPPER.readValue(itemDescJson, ItemDesc.class);
		System.out.println(itemDesc2);
		
	}
	
	//@SuppressWarnings("unchecked")
	@Test
	public void list2JSON() throws JsonProcessingException {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(1000L);
		itemDesc1.setItemDesc("JSON测试");
		
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(1001L);
		itemDesc2.setItemDesc("JSON测试2");
		
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		
		String itemDescJson = MAPPER.writeValueAsString(list);
		System.out.println(itemDescJson);
		
		List<ItemDesc> itemDescList = MAPPER.readValue(itemDescJson, list.getClass());
		List<ItemDesc> itemDescList2 = MAPPER.readValue(itemDescJson, new TypeReference<List<ItemDesc>>() {});
		System.out.println(itemDescList);
		System.out.println(itemDescList2);
		
	}
}
