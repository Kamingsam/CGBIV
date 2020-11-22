package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
/**
 * 定义图片的vo对象
 * @author Administrator
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EasyUIImage {
	private Integer error;  //0成功 1失败
	private String url;
	private Integer width;
	private Integer height;
	
	//失败方法
	public static EasyUIImage fail() {
		return new EasyUIImage(1, null, null, null);
	}
	//成功方法
	public static EasyUIImage success(String url,Integer width,Integer height) {
		return new EasyUIImage(0, url, width, height);
	}
}
