package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_item_cat")
public class ItemCat extends BasePojo{
	@TableId(type = IdType.AUTO) //主键自增
	private Long id;
	private Long parentId; 	//父级ID
	private String name;
	private Integer status;
	private Integer sortOrder; //排序号
	private Boolean isParent; //是否为父级 true  false
}
