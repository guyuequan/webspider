package com.hq.spider.parser;

import com.hq.spider.util.SpiderConfig;

public class Parser {
	
	private int currentLevel;
	
	private ParserRule pRule;
	
	private String contentString;
	
	private String inputString;
	
	/**
	 * 
	 * @param contentString The target(HTML page) that should be parsed with Xpath.
	 * @param pRule The specific {@link ParserRule} for the target(HTML page).
	 * @param currentLevel
	 * @param inputString The result of previous parser-method. Consists of all the data which will be written into the final file. Split by {@link SpiderConfig.SPLIT_STRING}
	 */
	public Parser(String contentString,ParserRule pRule,int currentLevel,String inputString) {
		this.currentLevel = currentLevel;
		this.pRule = pRule;
		this.contentString = contentString;
		this.inputString = inputString;
	}
	
	public java.util.List<String> process() throws Exception{
		
		switch (currentLevel) {
			case 1:
				return pRule.parser1(contentString,inputString);
			case 2:
				return pRule.parser2(contentString,inputString);
			case 3:
				return pRule.parser3(contentString,inputString);
			case 4:
				return pRule.parser4(contentString,inputString);
			case 5:
				return pRule.parser5(contentString,inputString);
			default:
				return null;
		}

	}
	
}
