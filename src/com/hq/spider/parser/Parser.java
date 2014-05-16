package com.hq.spider.parser;



public class Parser {
	
	private int currentLevel;
	
	private Parserrule pRule;
	
	private String contentString;
	
	private String urlString;
	
	public Parser(String contentString,Parserrule pRule,int currentLevel,String urlString) {
		
		// TODO Auto-generated constructor stub
		this.currentLevel = currentLevel;
		this.pRule = pRule;
		this.contentString = contentString;
		this.urlString = urlString;
		
	}
	
	public java.util.List<String> process(){
		
		switch (currentLevel) {
		case 1:
			return pRule.parser1(contentString,urlString);
		case 2:
			return pRule.parser2(contentString,urlString);
		case 3:
			return pRule.parser3(contentString,urlString);
		case 4:
			return pRule.parser4(contentString,urlString);
		case 5:
			return pRule.parser5(contentString,urlString);
		default:
			return null;
		}

	}
	
}
