package com.hq.spider.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * @author huqian.hq
 *
 */
@SuppressWarnings("deprecation")
public class Httphandle {
	
	private HttpClient httpclient= null;
	
	private HttpResponse response = null;
	
	public Httphandle() {
		httpclient = new DefaultHttpClient();
	}	
	
	//get host
	public String getHost(String urlString){
		String hostString = "";
		if(urlString.split("/")[0]!=null)
			hostString = urlString.split("/")[2];
		//System.out.println(hostString);
		return hostString;
	}
	
	// get method
	public String  get(String urlString) 
			throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(urlString);
		setHeaders(httpGet,getHost(urlString));
		response = httpclient.execute(httpGet);
		return EntityUtils.toString( response.getEntity());
	}
	
	// post method
	public  String post(String urlString,Map<String, String>params) throws ClientProtocolException, IOException{ 
		
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry:params.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			qparams.add(new BasicNameValuePair(key,value));	
		}
		setHeaders(httpPost,getHost(urlString));
		UrlEncodedFormEntity params_input = new UrlEncodedFormEntity(qparams, "UTF-8");
		httpPost.setEntity(params_input);	
		response = httpclient.execute(httpPost);
		return EntityUtils.toString(response.getEntity(),SpiderConfig.DEFAULT_CHARSET);
	}
	
	// close httpclient
	public void close(){
		httpclient.getConnectionManager().shutdown();
	}
	
	//set headers for httpGet
	private void setHeaders(HttpGet httpGet,String hostString){
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.addHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");	
		httpGet.addHeader("Cache-Control", "max-age=0");		
		httpGet.addHeader("Connection", "keep-alive");	
		//httpGet.addHeader("Cookie",cookieString);		
		if(hostString!="")
			httpGet.addHeader("Host", hostString);
		httpGet.addHeader("User-Agent",SpiderConfig.DEFAULT_AGENT);
	}
	
	// set headers for httpPost
	private void setHeaders(HttpPost httpPost,String hostString){
		httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//httpPost.addHeader("Accept-Encoding", "gzip, deflate");		
		httpPost.addHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");	
		httpPost.addHeader("Cache-Control", "max-age=0");		
		httpPost.addHeader("Connection", "keep-alive");	
		if(hostString!="")
			httpPost.addHeader("Host", hostString);
		//httpPost.addHeader("Cookie",cookieString);
		httpPost.addHeader("User-Agent",SpiderConfig.DEFAULT_AGENT);
	}
}
