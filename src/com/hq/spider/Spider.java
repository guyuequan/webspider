package com.hq.spider;

import java.util.ArrayList;
import java.util.List;

import com.hq.spider.parser.Parserrule;

/**
 * main spider,�����
 * @author huqian.hq
 * input :  enterurl + parserRuleMode + levels
 */
public class Spider {
		
	
	private  int levels;//����
	
	private String enterUrl  = null;//���룺���rul
	
	private List<String> enterList = null;//���룺���url list
	
	private Parserrule pRule ;//��������
	
	List<String> resultList ;//������
	
	private String pathString;//��������ַ
	
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
		startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��
		if(enterUrl !=null){
			System.out.println("[------��ǰ���в�:1--------]");
			Levelspider topLevelspider = new Levelspider(enterUrl, pRule,1,levels,pathString);
			topLevelspider.run();
			tmpList = topLevelspider.getResult();
		}else{
			Levelspider topLevelspider = new Levelspider(enterList, pRule,1,levels,pathString);
			topLevelspider.run();
			tmpList = topLevelspider.getResult();
		}
		endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		wholeTime+= endTime-startTime;
		System.out.println("[------��һ���ʱ:"+(endTime-startTime)+"ms---]");
		//other levels
		for (int i = 2; i <=levels; i++) {
			startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��
			System.out.println("[------��ǰ���в�:"+i+"-------]");
			Levelspider tmpLevelspider = new Levelspider(tmpList, pRule,i,levels,pathString);
			tmpLevelspider.run();
			tmpList.clear();
			tmpList = tmpLevelspider.getResult();
			endTime=System.currentTimeMillis(); //��ȡ����ʱ��
			wholeTime+= endTime-startTime;
			System.out.println("[------��ǰ�� "+i+" ��ʱ:"+(endTime-startTime)+"ms---]");
		}
		resultList = tmpList;
		System.out.println("��------���������������ʱ:"+wholeTime/1000+"  s-----��");
	}

}
