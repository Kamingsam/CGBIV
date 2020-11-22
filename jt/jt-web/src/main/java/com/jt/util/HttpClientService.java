package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;

	/**
	 * 需求:能否编辑一个HttpClientAPI,简化程序远程调用的过程.
	 * 简化策略:
	 * 		用户使用:传递参数
	 * 		结果: 远程访问的返回值.
	 * 参数说明:
	 * 	arg0:url地址,
	 * 	arg1:Map<key,value>  封装参数
	 * 	arg2:指定字符集编码      
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public String doGet(String url,Map<String,String> params,String charset){
		String result = null;
		//1.判断字符集编码是否为空 如果为空则给定默认值utf-8
		if(StringUtils.isEmpty(charset)){

			charset = "UTF-8";
		}
		//2.判断用户是否需要传递参数
		/**
		 * 2.校验Map中是否有参数.
		 * 没有参数: http://www.jt.com/findItem;
		 * 有参数:	http://www.jt.com/findItem?id=1&name=tomcat
		 * Map<entry<key,value>>
		 */
		if (params != null) {
			url +="?";
			for (Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				String paramsString = key+"="+value+"&";
				url += paramsString;//拼接参数
			}
			//http://www.jt.com/findItem?id=1&name=tomcat& 去除多有的&符
			url = url.substring(0, url.length()-1);
			System.out.println(url);
		}
		
//		if(params != null){
//			try {
//				URIBuilder uriBuilder = new URIBuilder(url);
//				for (Map.Entry<String,String> entry : params.entrySet()) {
//
//					uriBuilder.addParameter(entry.getKey(), entry.getValue());
//				}
//				//url?id=1&name=tom
//				url = uriBuilder.build().toString();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

		//3.定义参数提交对象
		HttpGet get = new HttpGet(url);

		//4.为请求设定超时时间
		get.setConfig(requestConfig);

		//5.通过httpClient发送请求
		try {
			CloseableHttpResponse response = 
					httpClient.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				//表示程序调用成功
				result = EntityUtils.toString(response.getEntity(),charset);
			}else{
				System.out.println("调用异常:状态信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String doGet(String url){

		return doGet(url, null, null);
	}

	public String doGet(String url,Map<String,String> params){

		return doGet(url, params, null);
	}

	public String doGet(String url,String charset){

		return doGet(url, null, charset);
	}

	//实现httpClient POST提交
	public String doPost(String url,Map<String,String> params,String charset){
		String result = null;

		//1.定义请求类型
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);  	//定义超时时间

		//2.判断字符集是否为null
		if(StringUtils.isEmpty(charset)){

			charset = "UTF-8";
		}

		//3.判断用户是否传递参数
		if(params !=null){
			//3.2准备List集合信息
			List<NameValuePair> parameters = 
					new ArrayList<>();

			//3.3将数据封装到List集合中
			for (Map.Entry<String,String> entry : params.entrySet()) {

				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			//3.1模拟表单提交
			try {
				UrlEncodedFormEntity formEntity = 
						new UrlEncodedFormEntity(parameters,charset); //采用u8编码

				//3.4将实体对象封装到请求对象中
				post.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}

		//4.发送请求
		try {
			CloseableHttpResponse response = 
					httpClient.execute(post);

			//4.1判断返回值状态
			if(response.getStatusLine().getStatusCode() == 200) {

				//4.2表示请求成功
				result = EntityUtils.toString(response.getEntity(),charset);
			}else{
				System.out.println("获取状态码信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}



	public String doPost(String url){

		return doPost(url, null, null);
	}

	public String doPost(String url,Map<String,String> params){

		return doPost(url, params, null);
	}

	public String doPost(String url,String charset){

		return doPost(url, null, charset);
	}
}
