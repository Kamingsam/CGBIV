package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.EasyUIImage;
@PropertySource(value = "classpath:/properties/image.properties")
@Service
public class FileServiceImpl implements FileService{
	@Value("${image.fileDirPath}")
	private String fileDirPath;//="G:/JT_IMAGE/"; //定义文件根目录
	@Value("${image.urlPath}")
	private String urlPath;//="http://image.jt.com/"; //定义虚拟文件根目录


	/**
	 * 实现思路:
	 * 	1.校验是否为图片 jpg|png|gif...
	 *  2.防止恶意程序
	 *  3.分目录保存图片 1.按照图片类型  2.按照时间划分 yyyy/MM/dd
	 *  4.防止图片重名    1.原有名称+随机数3位.jpg 2.UUID.jpg
	 */
	@Override
	public EasyUIImage uploadFile(MultipartFile uploadFile) {
		//一.实现图片校验
		//1.1 获取图片名称 a.jpg A.JPG  判断字符串的类型
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();	//全部转化为小写
		//1.2利用正则表达式判断是否为图片
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			//证明不是图片
			return EasyUIImage.fail();
		}

		//二.判断是否为一个恶意程序,可以根据宽度与高度实现图片的校验(宽高为图片必有属性)
		//准备一个图片的容器，将字节信息加到容器中，获取宽和高
		try {
			BufferedImage bufferImage = ImageIO.read(uploadFile.getInputStream());

			int width = bufferImage.getWidth();
			int height = bufferImage.getHeight();
			//判断
			if(width==0 || height==0) {
				//不是图片
				return EasyUIImage.fail();
			}
			//三.分目录保存图片 说明图片类型正确. 分目录存储. date转化为yyyy/MM/dd
			String datePath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
			//E:\JT_IMAGE\yyyy\MM\dd\
			String fileLocalPath = fileDirPath+datePath;
			File dirFile = new File(fileLocalPath);
			if(!dirFile.exists()) {
				//创建新目录
				dirFile.mkdirs();
			}
			//四.实现图片上传 UUID.jpg
			String uuid = UUID.randomUUID().toString().replace("-", "");
			//获取后缀
			int index = fileName.lastIndexOf(".");
			String fileType = fileName.substring(index);//.jpg
			String realName = uuid+fileType;
			//准备文件上传的全路径 E:\JT_IMAGE\2020\02\12\ uuid.jpg
			String realFilePath = fileLocalPath+realName;
			//文件上传
			uploadFile.transferTo(new File(realFilePath));
			//准备虚拟路径 http://image.jt.com/yyyy/MM/dd/uuid.jpg
			String url=urlPath+datePath+realName;//图片虚拟地址
			return EasyUIImage.success(url, width, height);
		} catch (IOException e) {
			e.printStackTrace();
			return EasyUIImage.fail();
		}

		//return null;

	}

}
