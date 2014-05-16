package com.hq.spider.downloader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.hq.spider.parser.Parser;
import com.hq.spider.parser.Parserrule;
import com.hq.spider.util.Spiderconfig;

/**
 * ���������̣߳���װ
 * @author huqian.hq
 *
 */
public class downloadThread implements Runnable{


	public String urlString;
	public int sleepTime =Spiderconfig.SLEEP_TIME; //����ʱ��
	public int retryCount=Spiderconfig.RETRY_COUNT; //��ʱ����
	private int currentLevel;//��ǰ����Ĳ���	
	private Parserrule pRule;
	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
	
	//constructor1
	public downloadThread(String urlString,int sleepTime,int retryCount,ConcurrentLinkedQueue<String> queue,Parserrule pRule,int currentLevel) {
		// TODO Auto-generated constructor stub
		this.urlString = urlString;
		this.sleepTime = sleepTime;
		this.retryCount = retryCount;
		this.queue = queue;
		this.pRule  = pRule;
		this.currentLevel = currentLevel;
	}
	/**
	 * 
	 * @param urlString  ����
	 * @param outputQueue ���
	 * @param pRule  ��������
	 * @param currentLevel ��ǰ��
	 */
	public downloadThread(String urlString,ConcurrentLinkedQueue<String> outputQueue,Parserrule pRule,int currentLevel){
		this.urlString = urlString;
		this.queue = outputQueue;
		this.pRule = pRule;
		this.currentLevel = currentLevel;
	}
	
	public String getUrl(){
		return this.urlString;
	}
	
	/**
	 * ����http����ͷ��Ϣ
	 * @author huqian.hq
	 **/	
	public Map<String, String> getHeader(){	
		Map<String, String> mp = new HashMap<>();
		mp.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		mp.put("Accept-Encoding","gzip, deflate");
		mp.put("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		mp.put("Cache-Control","max-age=0");
		mp.put("Connection","keep-alive");
		String tmp[] = urlString.split("/");
		mp.put("Host",tmp[2]);		
		String cookieString = "	__utma=; __utmz=; bid=\"Nj9abmZQo/g\"; ll=\"108296\"; __utma=30149280.1550421253.1398653447.1398680034.1399786189.3; __utmz=30149280.1398653447.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=223695111.1872449541.1398653484.1398680034.1399786189.3; __utmz=223695111.1398653484.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmb=30149280.2.10.1399786189; __utmc=30149280; __utmb=223695111.0.10.1399786189; __utmc=223695111";
		mp.put("Cookie",cookieString);
		mp.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		return mp;
	}
	
	
	/**
	 * ����url ��ȡ��Ϣ
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getInfo() throws ClientProtocolException, IOException{
		// http get request
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(urlString);
		Map<String, String> headers = getHeader();
		for (String key : headers.keySet()) {
		    String value = headers.get(key);
		    httpGet.addHeader(key, value);		
		}
		CloseableHttpResponse getResponse = httpclient.execute(httpGet);

		try {
		    int code = getResponse.getStatusLine().getStatusCode();
		    if (code == 200) {
		    	 HttpEntity entity1 = getResponse.getEntity();
		    	 
		    	  Charset tt = ContentType.getOrDefault(entity1).getCharset();
		    	  String charsetString;
		    	 if(tt==null)
		    		 charsetString = "gbk";
		    	 else {
		    		 charsetString = tt.toString();
				}
				    List<String> reusltList= (List<String>) new Parser(EntityUtils.toString(entity1,charsetString), pRule, currentLevel, urlString).process();
						 for (String string : reusltList) {
								 queue.add(string);
						 } 
				
			}else {
				System.err.println(urlString+" "+getResponse.getStatusLine());
			}
		   
		} finally {
		    getResponse.close();
		}
	}
	
	@Override
	public void run() {		
		// TODO Auto-generated method stub
		try {
				getInfo();
		} catch (Exception e) {
			// TODO: handle exception
			// ��Ϣ����
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			retryCount--;
			if(retryCount>0){
				run();
			}
			else {
				System.err.println("error: level:"+currentLevel+" website:"+urlString);
				e.printStackTrace();
			}

		}
	}	
}
