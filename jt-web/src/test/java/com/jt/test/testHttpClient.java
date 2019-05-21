package com.jt.test;

import java.io.IOException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class testHttpClient {
	/**
	 * 1.创建httpclient对象
	 * 2.创建请求对象 get/post
	 * 3.指定URL请求路径
	 * 4.发起request请求,获取response响应
	 * 5.判断请求是否正确:状态码是否为200
	 * 	TRUE 则通过response对象获取响应
	 * 	FALSE 则抛出异常 编辑日志
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClients.createDefault();
		String url = "http://manage.jt.com/web/item/findItemById/562379";
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response =  httpClient.execute(httpGet);
		if(response.getStatusLine().getStatusCode()==200) {
			System.out.println("恭喜你获取数据成功");
			System.out.println(EntityUtils.toString(response.getEntity()));
		}else
			System.out.println("获取失败");
		
	}
}
