package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.EasyUIImage;

@RestController
public class FileController {
	@Autowired
	private FileService fileService;
	
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws Exception, IOException {
		//1.获取文件名
		String fileName = fileImage.getOriginalFilename();
		//2.设置文件上传路径
		File dirFile = new File("G:\\TCGBIV\\TCGBIVDOCS\\day8\\testFile");
		//2.1判断文件是否存在
		if(!dirFile.exists()) {
			dirFile.mkdirs();//不存在则创建
		}
		//3.实现文件上传 G:\TCGBIV\TCGBIVDOCS\day8\testFilefileName
		String filePath="G:\\TCGBIV\\TCGBIVDOCS\\day8\\testFile\\"+fileName;
		fileImage.transferTo(new File(filePath));
		
		
		return "调用成功！！！";
	}
	/**
	 * url:http://localhost:8091/pic/upload?dir=image 
	 * 参数：uploadFile
	 * 返回值 EasyUIImage
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping("/pic/upload")
	public EasyUIImage uploadFile(MultipartFile uploadFile) {
		return fileService.uploadFile(uploadFile);
	}
}
