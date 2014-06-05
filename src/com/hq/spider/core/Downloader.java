package com.hq.spider.core;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.http.client.ClientProtocolException;
import com.hq.spider.parser.Parser;
import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.Httphandle;
import com.hq.spider.util.SpiderConfig;

/**
 * @author huqian.hq
 */
public class Downloader implements Runnable{
	
	//input url
	public String urlString;
	
	//sleep time
	public int sleepTime =SpiderConfig.SLEEP_TIME; 
	
	//retry connect count
	public int retryCount=SpiderConfig.RETRY_COUNT; 
	
	//current level
	private int currentLevel;
	
	//input parser rule
	private ParserRule pRule;
	
	//input string
	private String inputString ="";
	
	//output queue
	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
	
	//constructor1
	/**
	 * constructor complicated
	 * @param inputString
	 * @param sleepTime
	 * @param retryCount
	 * @param queue
	 * @param pRule
	 * @param currentLevel
	 */
	public Downloader(String inputString,int sleepTime,int retryCount,
			ConcurrentLinkedQueue<String> queue,ParserRule pRule,int currentLevel) {
		if(inputString.indexOf(SpiderConfig.SPLIT_STRING)>0)
			this.urlString = inputString.split(SpiderConfig.SPLIT_STRING)[0];
		else
			this.urlString = inputString;
		this.inputString = inputString;
		this.sleepTime = sleepTime;
		this.retryCount = retryCount;
		this.queue = queue;
		this.pRule  = pRule;
		this.currentLevel = currentLevel;
	}
	/**
	 * constructor simple
	 * @param inputString  
	 * @param outputQueue 
	 * @param pRule  
	 * @param currentLevel
	 * 
	 */
	public Downloader(String inputString,ConcurrentLinkedQueue<String> outputQueue,
			ParserRule pRule,int currentLevel){
		if(inputString.indexOf(SpiderConfig.SPLIT_STRING)>0){
			this.urlString = inputString.split(SpiderConfig.SPLIT_STRING)[0];
		}
		else
			this.urlString = inputString;
		this.inputString = inputString;
		this.queue = outputQueue;
		this.pRule = pRule;
		this.currentLevel = currentLevel;
	}
	
	/**
	 * @return 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String downloadHtml() throws ClientProtocolException, IOException{
		//start html download
		Httphandle httphandle = new Httphandle();
		String contentString = httphandle.get(urlString);
		httphandle.close();
		return contentString;
	}
	
	@Override
	public void run() {
		
		String contentString="";	
		//start downloader
		try {
			contentString =downloadHtml();
			
		} catch (Exception e) {// connection error: reconnect
			e.printStackTrace();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			retryCount--;
			if(retryCount>0){
				run();
			}
			else {
				System.err.println("[ERROR] Connection : level "+currentLevel+"=>"+urlString);
				e.printStackTrace();
			}
		}
		//start the parser
		String charsetString =SpiderConfig.DEFAULT_CHARSET;// getCharset(contentString).toUpperCase();		
		try {
			//encoding
			List<String> reusltList= null;
			if(charsetString != SpiderConfig.DEFAULT_CHARSET){
				String newString = new String(contentString.getBytes("iso8859-1"),charsetString);
				reusltList = (List<String>) new Parser(newString, pRule, currentLevel, inputString).process();
			}else {
				reusltList = (List<String>) new Parser(contentString, pRule, currentLevel, inputString).process();
			}
			//add to the queue
			for (String string : reusltList) {
				queue.add(string);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] parser,level"+this.currentLevel+" website:"+this.urlString);
			e1.printStackTrace();
		}
		//sleep for the next downloader
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}	
	
	@SuppressWarnings("unused")
	private String getCharset(String contentString) {
	   	 int t1 = contentString.indexOf("charset=");
	   	 int k = t1;
	   	 
	   	 while((contentString.charAt(k)!='"'&&contentString.charAt(k)!=';'&&contentString.charAt(k)!=' ')&&k<=contentString.length())
	   		 k++;
	   	 
	   	 String charsetString = "";
	   	 if (k>t1&&t1>0)
	   		 charsetString = contentString.substring(t1+8,k);
	   	 if (charsetString.equals("gb2312")){
	   		 charsetString = "gbk";
	   	 }
		return charsetString;
	}
}
