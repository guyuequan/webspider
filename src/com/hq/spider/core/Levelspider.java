package com.hq.spider.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.Filehandle;
import com.hq.spider.util.SpiderConfig;

/**
 * per lever spider : input the urls and return the results  
 * @author huqian.hq
 * input :  url or urlList (String or List<String>)  
 * output:  (Queue<String>)
 */
public class Levelspider implements Runnable{

	private ConcurrentLinkedDeque<String> inputQueue = new ConcurrentLinkedDeque<String>();
	
	//contain the results of Parser, using the corresponding ParserRule.
	private ConcurrentLinkedQueue<String> outputQueue = new ConcurrentLinkedQueue<String>();
	
	private ParserRule pRule; // parser rule : contain the whole levels
	
	private int currentLevel ; //current level
	
	private int maxLevel ; //max level
		
	private int threadCount = SpiderConfig.THREAD_COUNT;//spider max thread 
	
	private Filehandle filehandle = null;
	
	private String pathString = null;//output address
		
	//constructor 1
	public Levelspider(String urlString,ParserRule pRule,int currentLevel,int maxLevel,String pathString){
		inputQueue.push(urlString);
		this.pRule = pRule;
		this.currentLevel = currentLevel;
		this.maxLevel = maxLevel;
		this.pathString = pathString;
	}
	//constructor 2
	public Levelspider(List<String>urlList,ParserRule pRule,int currentLevel,int maxLevel,String pathString){
		for (String string : urlList) {
			inputQueue.push(string);			
		}
		this.pathString = pathString;
		this.pRule = pRule;
		this.maxLevel = maxLevel;
		this.currentLevel = currentLevel;
	}
	
	@Override
	public void run() {
			/// start
			ExecutorService executorService = Executors.newFixedThreadPool(this.threadCount);
			while(!inputQueue.isEmpty()){
				Downloader tmpDownloadThread  = new Downloader(inputQueue.poll(), outputQueue, pRule, currentLevel);
				executorService.execute(tmpDownloadThread);
			}
			
			executorService.shutdown();
			
			if(this.currentLevel == maxLevel&&filehandle==null){
				filehandle =  new Filehandle(this.pathString, outputQueue);
				new Thread(filehandle).start();
			}
			
			//loop judge: whether it is terminated 
			try {
				while(!executorService.awaitTermination(100, TimeUnit.SECONDS)){
					//do nothing
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//level spider over
			if(this.currentLevel == maxLevel)
				filehandle.close();
	}

	/**
	 * return the result of this level
	 * @return
	 */
	public List<String > getResult(){
		List<String> result = new ArrayList<String>();
		for (String string : outputQueue) {
			result.add(string);
		}
		return result;
	}

}
