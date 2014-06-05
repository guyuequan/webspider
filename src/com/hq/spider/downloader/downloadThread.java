package com.hq.spider.downloader;

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
public class downloadThread implements Runnable{
	
	//输入url
	public String urlString;
	
	//休息时间
	public int sleepTime =SpiderConfig.SLEEP_TIME; 
	
	//重连次数
	public int retryCount=SpiderConfig.RETRY_COUNT; 
	
	//当前层数
	private int currentLevel;
	
	//输入解析器规则
	private ParserRule pRule;
	
	//输入
	private String inputString ="";
	
	//输出结果队列
	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
	
	//constructor1
	/**
	 * 复杂可配置版，构造函数
	 * @param inputString
	 * @param sleepTime
	 * @param retryCount
	 * @param queue
	 * @param pRule
	 * @param currentLevel
	 */
	public downloadThread(String inputString,int sleepTime,int retryCount,
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
	 * 简单版，构造函数
	 * @param inputString  
	 * @param outputQueue 
	 * @param pRule  
	 * @param currentLevel
	 * 
	 */
	public downloadThread(String inputString,ConcurrentLinkedQueue<String> outputQueue,
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
		//start downloader
		Httphandle httphandle = new Httphandle();
		String contentString = httphandle.get(urlString);
		return contentString;
	}
	
	@Override
	public void run() {
		
		String contentString="";	
		
		//start the downloader
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
		String charsetString = getCharset(contentString);
		System.out.println(charsetString);
		String newString;
		
		try {
			//encoding
			newString = new String(contentString.getBytes("iso8859-1"),charsetString);
			List<String> reusltList = (List<String>) new Parser(newString, pRule, currentLevel, inputString).process();
			for (String string : reusltList) {
				queue.add(string);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] parser,level"+this.currentLevel+" website:"+this.urlString);
			//e1.printStackTrace();
		}
		//sleep for the next downloader
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}	
	
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
