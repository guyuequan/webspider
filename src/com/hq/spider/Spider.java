package com.hq.spider;

import java.util.ArrayList;
import java.util.List;

import com.hq.spider.parser.ParserRule;

/**
 * main spider
 * @author huqian.hq
 * input :  enterurl + parserRuleMode + levels
 */
public class Spider {
		
	
	private  int levels;//max levels
	
	private String enterUrl  = null;//enter rul
	
	private List<String> enterList = null;//enter url list
	
	private ParserRule pRule ;//parser rule
	
	List<String> resultList ;//output result
	
	private String pathString;//output address
	
	//constructor
	public Spider(String enterUrl,String pathString,ParserRule pRule,int levels) {
		// TODO Auto-generated constructor stub
		this.enterUrl = enterUrl;
		this.levels = levels;
		this.pRule = pRule;
		this.pathString = pathString;
	}
	
	//constructor
	public Spider(List<String>enterList,String pathString ,ParserRule pRule,int levels){
		this.enterList = enterList;
		this.pathString = pathString;
		this.levels = levels;
		this.pRule = pRule;
	}
	
	
	public  void process(){
		
		//top level 
		List<String> tmpList = new ArrayList<String>();
		long startTime,endTime,wholeTime = 0;
		startTime=System.currentTimeMillis();  
		System.out.println("[------current level:1--------]");
		if(enterUrl !=null){
			Levelspider topLevelspider = new Levelspider(enterUrl, pRule,1,levels,pathString);
			topLevelspider.run();
			tmpList = topLevelspider.getResult();
		}else{
			Levelspider topLevelspider = new Levelspider(enterList, pRule,1,levels,pathString);
			topLevelspider.run();
			tmpList = topLevelspider.getResult();
		}
		endTime=System.currentTimeMillis(); 
		wholeTime+= endTime-startTime;
		System.out.println("[------level 1 cost:"+(endTime-startTime)+"ms---]");
		//other levels
		for (int i = 2; i <=levels; i++) {
			startTime=System.currentTimeMillis();   
			System.out.println("[------level:"+i+"-------]");
			Levelspider tmpLevelspider = new Levelspider(tmpList, pRule,i,levels,pathString);
			tmpLevelspider.run();
			tmpList.clear();
			tmpList = tmpLevelspider.getResult();
			endTime=System.currentTimeMillis(); 
			wholeTime+= endTime-startTime;
			System.out.println("[------level"+i+" cost:"+(endTime-startTime)+"ms---]");
		}
		resultList = tmpList;
		System.out.println("[-----Spider over, cost:"+wholeTime/1000+"  s-----]");
	}

}
