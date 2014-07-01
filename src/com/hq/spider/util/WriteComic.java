package com.hq.spider.util;

import java.awt.image.TileObserver;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class WriteComic {
	
		public static boolean writeData(String jsonString) throws JSONException, ClientProtocolException, IOException{
			String urlString2 = "http://dm302.sinaapp.com/db/write.php?secret=dm302&";
    		Httphandle httphandle = new Httphandle();
    		Map<String,String> params = new HashMap<String, String>();
    		params.put("value", jsonString);
    		String resultString = httphandle.post(urlString2, params);
    		JSONObject jsonObject = new JSONObject(resultString);
    		String code1 = (String) jsonObject.get("code");
    		//System.out.println(code1);
    		if(code1.startsWith("failed")){
    			return false;
    		}
			return true;
		}
	
		
		public static void writeFromFile(String dirString,String domainString,String dateString) throws IOException, JSONException, InterruptedException{
			
			String filePath = dirString+"/"+domainString+"/"+dateString+".txt";
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			String line = reader.readLine();
			Set<String> titleSet = new HashSet<String>();
			int errCnt = 0;
			int succCnt = 0;
			while(line!=null){
				 JSONObject jsonObject1 = new JSONObject(line);
				 if(jsonObject1.isNull("title")){
					 line = reader.readLine();
	    			 continue;
				 }
	    		 String title = jsonObject1.getString("title");
	    		 if(titleSet.contains(title)){
	    			 System.out.println(title);
	    			 line = reader.readLine();
	    			 continue;
	    		 }
	    		 else
	    			 titleSet.add(title);
	    		System.out.println("[WRITE] "+title);
	    		 
	    		String urlString2 = "http://dm302.sinaapp.com/db/write.php?secret=dm302&";
	    		Httphandle httphandle = new Httphandle();
	    		Map<String,String> params = new HashMap<String, String>();
	    		params.put("value", line);
	    		String resultString = httphandle.post(urlString2, params);
	    	//	System.out.println(resultString);
	    		try {
					JSONObject jsonObject = new JSONObject(resultString);
		    		String code1 = (String) jsonObject.get("code");
		    		//System.out.println(code1);
		    		if(code1.startsWith("failed")){
		    			errCnt+=1;
		    			String outputString = "[ERROR]"+resultString+"\t"+line;
		    			System.err.println(outputString.trim());
		    		}else{
		    			succCnt += 1;
		    			//System.out.println(succCnt);
		    		}
				} catch (Exception e) {
					// TODO: handle exception
					System.err.println(resultString);
					e.printStackTrace();
				}			    	
	    		Thread.sleep(10);
				line = reader.readLine();
			}

	    	 System.out.println("titles "+titleSet.size());
	    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	 String nowTime = df.format(new Date());
	    	 System.out.println("[RESULT]"+nowTime+"\tsuccess:"+succCnt+"\tfailed:"+errCnt);
		}
		
		
		
		public static void writeFromUrl(String urlString) throws ClientProtocolException, IOException{
			HttpClient httpclient= new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(urlString);
			HttpResponse getResponse = httpclient.execute(httpGet);			
			try {
			    int code = getResponse.getStatusLine().getStatusCode();
			    if (code == 200) {
			    	 HttpEntity entity1 = getResponse.getEntity();			    	 
			    	 String lineString = EntityUtils.toString(entity1,"utf-8");
			    	 String tmp[] = lineString.split("\n");
			    	 int errCnt =0, succCnt = 0;
			    	 Set<String> titleSet = new HashSet<String>();
			    	 for (String string : tmp) {
			    		 //System.out.println(string);
			    		 JSONObject jsonObject1 = new JSONObject(string);
			    		 String title = (String)jsonObject1.get("title");
			    		 if(titleSet.contains(title)){
			    			 System.out.println(title);
			    			 continue;
			    		 }
			    		 else
			    			 titleSet.add(title);
			    		System.out.println("[WRITE] "+title);
			    		//write data through api
			    		 
			    		String urlString2 = "http://dm302.sinaapp.com/db/write.php?secret=dm302&";
			    		Httphandle httphandle = new Httphandle();
			    		Map<String,String> params = new HashMap<String, String>();
			    		params.put("value", string);
			    		String resultString = httphandle.post(urlString2, params);
			    	//	System.out.println(resultString);
			    		try {
							JSONObject jsonObject = new JSONObject(resultString);
				    		String code1 = (String) jsonObject.get("code");
				    		//System.out.println(code1);
				    		if(code1.startsWith("failed")){
				    			errCnt+=1;
				    			String outputString = "[ERROR]"+resultString+"\t"+string;
				    			System.err.println(outputString.trim());
				    		}else{
				    			succCnt += 1;
				    			//System.out.println(succCnt);
				    		}
						} catch (Exception e) {
							// TODO: handle exception
							System.err.println(resultString);
							e.printStackTrace();
						}			    	
			    		Thread.sleep(10);
			    		
			    		//write over
					}
			    	 System.out.println("titles "+titleSet.size());
			    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	 String nowTime = df.format(new Date());
			    	 System.out.println("[RESULT]"+nowTime+"\tsuccess:"+succCnt+"\tfailed:"+errCnt);
			    }
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		public static void main(String[] args) throws ClientProtocolException, IOException, JSONException, InterruptedException {
			//from file
			String dirString = "d:/spider";
			String dateString = "20140616";
			ArrayList<String> domainList = new ArrayList<String>();
			//domainList.add("baidu");
			//domainList.add("tudou");
			//domainList.add("letv");
			//domainList.add("sohuTV");
			//domainList.add("youku");
			//domainList.add("xunleikankan");
			//domainList.add("pptv");
			//domainList.add("iqiyi");  //slow
			//domainList.add("dmm");
			
			for (String string : domainList) {
				System.out.println("[DOMAIN] "+string);
				writeFromFile(dirString,string,dateString);			
			}		
		}
}
