package com.hq.spider;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.util.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


/**
 * 新浪微博的登陆，
 * 输入：username + password 
 * 输出�? cookie
 * @author huqian.hq
 *
 */
public class SinaLogin {
	
	public static final DateFormat format = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
	Random r = new Random();
	private Pattern p = Pattern.compile("\\((.*?)\\)");
	private Pattern p0 = Pattern.compile("location.replace\\((.*?)\\)");
	
	HttpClient httpclient= null;
	
	List<Account> accounts = new ArrayList<Account>();
	List<String> cookieList = new ArrayList<String>();

	public SinaLogin() {
		// TODO Auto-generated constructor stub
		for (Entry<String, String> entry: AccountConfig.userPassMap.entrySet()) {
		    String username = entry.getKey();
		    String password = entry.getValue();
		    String tmpCookie = login(username, password);
		    System.out.println(tmpCookie);
		    cookieList.add(tmpCookie);
		}			
	}
	
	/**
	 * return cookie list
	 * @return
	 */
	public List<String> getCookieList(){
		return cookieList;
	}
	
	/**
	 * main 
	 * @param args
	 */
	/*
	public static void main(String[] args){
		SinaLogin testLogin = new SinaLogin();
		
	}*/
	
	
	/**
	 *输入地址 �? cookie，测试微�?
	 */
	public static void getWeibo(String urlString,String cookie){
		HttpClient httpclient1= new DefaultHttpClient();	
		HttpClientParams.setCookiePolicy(httpclient1.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);   
		HttpGet httpGet = new HttpGet(urlString);
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	//	httpGet.addHeader("Accept-Encoding", "gzip, deflate");		
		httpGet.addHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");	
		httpGet.addHeader("Cache-Control", "max-age=0");		
		httpGet.addHeader("Connection", "keep-alive");	
		httpGet.addHeader("Cookie", cookie);		
		httpGet.addHeader("Host", "weibo.com");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		try {
			HttpResponse getResponse = httpclient1.execute(httpGet);
			//输出结果
			System.out.println(EntityUtils.toString(getResponse.getEntity(),"utf-8"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
		
	/**
	 * get result by method get
	 * @param urlString
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	String getResult(String urlString) throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(urlString);
		HttpResponse getResponse = httpclient.execute(httpGet);			
		String result = "";
		try {
		    int code = getResponse.getStatusLine().getStatusCode();
		    if (code == 200) {
		    	 HttpEntity entity1 = getResponse.getEntity();
		    	 result = EntityUtils.toString(entity1,"utf-8");
		    }
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	

	
	/**
	 * get result by method Post
	 * @param url
	 * @param map
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	String getResultByPost(String url,Map<String, String> map ) throws ClientProtocolException, IOException{
		
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
		
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			nvps.add(new BasicNameValuePair(key, val));  
		}
	    httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
	    HttpResponse getResponse = httpclient.execute(httpPost);			
		String result = "";
		try {
		    int code = getResponse.getStatusLine().getStatusCode();
		    if (code == 200) {
		    	 HttpEntity entity1 = getResponse.getEntity();
		    	 result = EntityUtils.toString(entity1,"utf-8");
		    	// System.out.println(EntityUtils.toString(entity1,"utf-8"));
		    }
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	/**
	 * 登陆，返�? cookie
	 * @param username
	 * @param password
	 * @return
	 */
   public String login(String username,String password){
	   httpclient= new DefaultHttpClient();	
    	//String cookieResult = "SINAGLOBAL=; km_ai=; km_uq=; SUB=; SUBP=; ALF=; ULV=; UOR=; UOR=login.sina.com.cn,weibo.com,login.sina.com.cn; SINAGLOBAL=7764129300619.424.1400909154042; ULV=1400910939263:1:1:1:7764129300619.424.1400909154042:;WBtopGlobal_register_version=0636b49c5cc991b7; login_sid_t=719ca5587433fcc301f5d28a9e4c624c; appkey=; YF-V5-G0=c37fc61749949aeb7f71c3016675ad75; ALF=1432446925; wvr=5;un=huqian217@gmail.com; __utma=15428400.1331476702.1400837900.1400837900.1400837900.1; __utmz=15428400.1400837900.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic|utmctr=%E5%BE%AE%E5%8D%9A%20%E5%90%8D%E4%BA%BA%E5%A0%82;YF-Page-G0=f48b81114eb6409906d5e133ec75f400; _s_tentry=login.sina.com.cn; Apache=7764129300619.424.1400909154042;";
		String cookieResult = "";
    	String responstr = null;		
		try {
			String url = "http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su="+ encodeUserName(username) + "&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.11)&_=" + System.currentTimeMillis();
			//第一次请�?
			responstr = getResult(url);
			String servertime = getCode(responstr, "servertime");
			String nonce = getCode(responstr, "nonce");
			String sin_pk = getCode(responstr, "pubkey");
			String rsakv = getCode(responstr, "rsakv");	
			String pwdString = servertime + "\t" + nonce + "\n" + password;
			String sp = new BigIntegerRSA().rsaCrypt(sin_pk, "10001", pwdString);
			String postURL = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.11)";
			// 登录POST参数
			Map<String, String> partam = new HashMap<String, String>(); 
			partam.put("entry", "weibo");
			partam.put("gateway", "1");                                                                                                                                                                                                                                                                                                            
			partam.put("savestate", "7");
			partam.put("from", "");
			partam.put("useticket", "1");
			partam.put("pagerefer", "http://weibo.com/a/download");
			partam.put("vsnf", "1");
			partam.put("su", encodeUserName(username));
			partam.put("service", "miniblog");
			partam.put("servertime", servertime);
			partam.put("nonce", nonce);
			partam.put("pwencode", "rsa2");
			partam.put("rsakv", rsakv);
			partam.put("sp", sp);
			partam.put("encoding", "UTF-8");
			partam.put("url", "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
			partam.put("eturntype", "META");
			partam.put("ssosimplelogin", "1");
			//第二次请�?
			String s =getResultByPost(postURL, partam);
			String result1 = getNexrURL(s);
			JSONObject jsonObj = new JSONObject("{"+result1+"}");
			Object checkResult = jsonObj.get("retcode");
		
			if((int)checkResult==0){
				String cookieUrl = getNexrURL0(s);
				//第三次请求，获取cookie
				cookieResult = getCookie(cookieUrl);
				System.out.println("登陆成功,获得cookie");
			}else{
				System.out.println("无法登陆,�?查是否密码错�?");
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return cookieResult;
	}

    
    ///内部方法
	private String getCode(String input, String mark) throws JSONException {
		
		String tmp = "sinaSSOController.preloginCallBack(";
		int s = input.indexOf(tmp);
		String jsonString =input.substring(s+tmp.length(),input.length()-1);
		JSONObject jsonObj = new JSONObject(jsonString);
		Object object = jsonObj.get(mark);
		if(object instanceof Integer)
			return String.valueOf(object);
		return (String) jsonObj.get(mark);
	}
    
	///内部方法
	private  String encodeUserName(String email) {
	    String userName = "";  
        try {  
        	email = email.replaceAll("@", "%40");
            userName = Base64.encode(email.getBytes());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return userName;  
	}

	//获取第一级地�?
	private  String getNexrURL(String s) {
		String url = "";
		if(!s.isEmpty()){
			Matcher m = p.matcher(s);
			if(m.find()){
				url = m.group();
			} 
		}
		if(url.startsWith("(")){
			url = url.substring(2, url.length() - 2);
		}
		return url;
	}
	
	//获取第二级地�?
	private String getNexrURL0(String s) {
		String url = "";
		if(!s.isEmpty()){
			
			Matcher m = p0.matcher(s);
			if(m.find()){
				url = m.group();
			} 
		}
		if(url.startsWith("location")){
			url = url.substring(18, url.length() - 2);
		}
		return url;
	}
	/**
	 * 第三级请求，获取cookie
	 * 输入�?终url ，取得cookie
	 * @param urlString
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	String getCookie(String urlString) throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(urlString);
		HttpResponse getResponse = httpclient.execute(httpGet);
		String result = "";
		for (Header header : getResponse.getHeaders("Set-Cookie")) {
			String tmp = header.getValue();
			result += tmp.split(";")[0]+";";
	
		}
		return result;
	}


	
}