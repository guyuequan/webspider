package com.hq.spider.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import us.codecraft.xsoup.Xsoup;

public class Xpath {
	
	String inputString;
	String xpathString;
	
	public Xpath(String inputString ,String xpathString) {
		// TODO Auto-generated constructor stub
		this.inputString = inputString;
		this.xpathString = xpathString;
	}
	
	/**
	 * get the result with html
	 * @return
	 */
	public String getResult(){
		String result = null;
		Document document = Jsoup.parse(inputString);
		result = Xsoup.compile(xpathString).evaluate(document).get();
		return result;
	}
	
	/**
	 * get the result without html
	 * @return
	 */
	public String getFilterHtmlResult(){
		String result = null;
		Document document = Jsoup.parse(inputString);
		result = Xsoup.compile(xpathString).evaluate(document).get();
		if(result != null){
			result = result.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
					"<[^>]*>", "");
			result = result.replaceAll("[(/>)<]", "");
		}
		return result;
	}

	


}
