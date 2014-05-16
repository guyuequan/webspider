package com.hq.spider;

import java.util.ArrayList;
import java.util.List;

import com.hq.spider.parser.Parserrule;

/**
 * main spider,主入口
 * @author huqian.hq
 * input :  enterurl + parserRuleMode + levels
 */
public class Spider {
		
	
	private  int levels;//层数
	
	private String enterUrl  = null;//输入：入口rul
	
	private List<String> enterList = null;//输入：入口url list
	
	private Parserrule pRule ;//解析规则
	
	List<String> resultList ;//输出结果
	
	private String pathString;//输出结果地址
	
	//constructor
	public Spider(String enterUrl,String pathString,Parserrule pRule,int levels) {
		// TODO Auto-generated constructor stub
		this.enterUrl = enterUrl;
		this.levels = levels;
		this.pRule = pRule;
		this.pathString = pathString;
	}
	
	//constructor
	public Spider(List<String>enterList,String pathString ,Parserrule pRule,int levels){
		this.enterList = enterList;
		this.pathString = pathString;
		this.levels = levels;
		this.pRule = pRule;
	}
	
	
	public  void process(){
		
		//top level 
		List<String> tmpList = new ArrayList<String>();
		long startTime,endTime,wholeTime = 0;
		startTime=System.currentTimeMillis();   //获取开始时间
		if(enterUrl !=null){
			System.out.println("[------当前运行层:1--------]");
			Levelspider topLevelspider = new Levelspider(enterUrl, pRule,1,levels,pathString);
			topLevelspider.run();
			tmpList = topLevelspider.getResult();
		}else{
			Levelspider topLevelspider = new Levelspider(enterList, pRule,1,levels,pathString);
			topLevelspider.run();
			tmpList = topLevelspider.getResult();
		}
		endTime=System.currentTimeMillis(); //获取结束时间
		wholeTime+= endTime-startTime;
		System.out.println("[------第一层耗时:"+(endTime-startTime)+"ms---]");
		//other levels
		for (int i = 2; i <=levels; i++) {
			startTime=System.currentTimeMillis();   //获取开始时间
			System.out.println("[------当前运行层:"+i+"-------]");
			Levelspider tmpLevelspider = new Levelspider(tmpList, pRule,i,levels,pathString);
			tmpLevelspider.run();
			tmpList.clear();
			tmpList = tmpLevelspider.getResult();
			endTime=System.currentTimeMillis(); //获取结束时间
			wholeTime+= endTime-startTime;
			System.out.println("[------当前层 "+i+" 耗时:"+(endTime-startTime)+"ms---]");
		}
		resultList = tmpList;
		System.out.println("【------总体爬虫结束，耗时:"+wholeTime/1000+"  s-----】");
	}

}
