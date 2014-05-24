package com.hq.spider.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.hq.spider.SinaLogin;

public class SpiderConfig {
	
	public static final int  SLEEP_TIME = 500;//spider config: sleep time
	
	public static final int RETRY_COUNT = 3;//retry connect count
	
	public static final int THREAD_COUNT = 5;//spider thread  counts
	
	public static final String DEFAULT_CHARSET="UTF-8";//GBK

	public static final String SPLIT_STRING="@@";
	
	public static List<String> cookieList = new ArrayList<String>();

	//��ʼ��
	public static void init(){
		SinaLogin sinaLogin = new SinaLogin();
		cookieList= sinaLogin.getCookieList();
	}
	
	/**
	 * ��ȡ΢����Ϣ
	 */
	public static String getWeibo(String urlString,String cookie){
		
		String result = "";
		HttpClient httpclient1= new DefaultHttpClient();	
		HttpClientParams.setCookiePolicy(httpclient1.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);   
		HttpGet httpGet = new HttpGet(urlString);
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	//	httpGet.addHeader("Accept-Encoding", "gzip, deflate");		
		httpGet.addHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");	
		httpGet.addHeader("Cache-Control", "max-age=0");		
		httpGet.addHeader("Connection", "keep-alive");	
		httpGet.addHeader("Cookie", cookie);		
		//httpGet.addHeader("Host", "weibo.com");
		httpGet.addHeader("Host", "m.weibo.cn");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		try {
			HttpResponse getResponse = httpclient1.execute(httpGet);
			//输出结果
			result = EntityUtils.toString(getResponse.getEntity(),"utf-8");
			//System.out.println(EntityUtils.toString(getResponse.getEntity(),"utf-8"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}
	

}
