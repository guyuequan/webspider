package com.hq.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.hq.spider.downloader.downloadThread;
import com.hq.spider.parser.Parserrule;
import com.hq.spider.util.Filehandle;
import com.hq.spider.util.Spiderconfig;

/**
 * ������棬����url list ������
 * per lever spider : input the urls and return the results  
 * @author huqian.hq
 * input :  url or urlList (String or List<String>)  
 * output:  (Queue<String>)
 */
public class Levelspider implements Runnable{

	private ConcurrentLinkedDeque<String> inputQueue = new ConcurrentLinkedDeque<String>();//�������
	
	private ConcurrentLinkedQueue<String> outputQueue = new ConcurrentLinkedQueue<String>();//�������
	
	
	private Parserrule pRule; // ���������������������в��
	
	private int currentLevel ; //��ǰ��
	
	private int maxLevel ; //����
		
	private int threadCount = Spiderconfig.THREAD_COUNT;//ץȡ�߳���
	
	private Filehandle filehandle = null;
	
	private String pathString = null;//�����ַ
	
	//constructor 1
	public Levelspider(String urlString,Parserrule pRule,int currentLevel,int maxLevel,String pathString){
		inputQueue.push(urlString);
		this.pRule = pRule;
		this.currentLevel = currentLevel;
		this.maxLevel = maxLevel;
		this.pathString = pathString;
	}
	//constructor 2
	public Levelspider(List<String>urlList,Parserrule pRule,int currentLevel,int maxLevel,String pathString){
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
				downloadThread tmpDownloadThread  = new downloadThread(inputQueue.poll(), outputQueue, pRule, currentLevel);
				executorService.execute(tmpDownloadThread);
			}
			
			executorService.shutdown();
			
			if(this.currentLevel == maxLevel&&filehandle==null){
				filehandle =  new Filehandle(this.pathString, outputQueue);
				new Thread(filehandle).start();
			}
			
			//loop judge: whether it is terminated 
			try {
				while(!executorService.awaitTermination(1, TimeUnit.SECONDS)){
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
