package com.hq.parser.website;

import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.hq.spider.parser.Parserrule;
import com.hq.spider.util.Xpath;

/**
 * the parser for doubanComic
 * @author huqian.hq
 *
 */
public class doubanComic implements Parserrule {
		
	
//	private String url = "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E6%BC%AB";// 
	
	@Override
	public List<String> parser1(String contentString,String urlString) {
		// TODO Auto-generated method stub
		List<String>  resultList = new ArrayList<String>();
		int offset = 20;
		String url = "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB?start=";
		//get pages
		int start = 51;
		int end = 60;
		for (int i = start; i <= end; i++) {			
			resultList.add(url+i*offset);
		}
		return resultList;
	}
	
	@Override
	public List<String> parser2(String contentString,String urlString) {
		// TODO Auto-generated method stub
		List<String>  resultList = new ArrayList<String>();
		int offset  =20;
		for (int i = 1; i <= offset; i++) {
			String xpathString = "/html/body/div[@id='wrapper']/div[@id='content']/div[@class='grid-16-8 clearfix']/div[@class='article']/div[2]/table["+i+"]/tbody/tr[@class='item']/td[2]/div[@class='pl2']/a";
			String linkNote = new Xpath(contentString, xpathString).getResult();
			java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<=(href=\")).*?(?=\")");
			if(linkNote!=null){
				Matcher m = p.matcher(linkNote); 
				m.find();
				resultList.add(m.group());
			}
		}
		return resultList;
	}

	@Override
	public List<String> parser3(String contentString,String urlString) {
		// TODO Auto-generated method stub
		List<String> resultList = new ArrayList<>();
		String xpathString = "/html/body/div[@id='wrapper']/div[@id='content']/div[@class='grid-16-8 clearfix']/div[@class='article']/div[@class='indent']/div[@class='subjectwrap clearfix']/div[@class='subject clearfix']/div[@id='info']";
		String imgXpath = "/html/body/div[@id='wrapper']/div[@id='content']/div[@class='grid-16-8 clearfix']/div[@class='article']/div[@class='indent']/div[@class='subjectwrap clearfix']/div[@class='subject clearfix']/div[@id='mainpic']/a[@class='nbgnbg']/img/@src";
		String commentXpath = "/html/body/div[@id='wrapper']/div[@id='content']/div[@class='grid-16-8 clearfix']/div[@class='article']/div[@class='indent']/div[@class='subjectwrap clearfix']/div[@id='interest_sectl']/div[@class='rating_wrap clearbox']/p[@class='rating_self clearfix']/strong[@class='ll rating_num']";
		String commentNumXpath = "/html/body/div[@id='wrapper']/div[@id='content']/div[@class='grid-16-8 clearfix']/div[@class='article']/div[@class='indent']/div[@class='subjectwrap clearfix']/div[@id='interest_sectl']/div[@class='rating_wrap clearbox']/p[2]/a/span";
		String briefXpath ="/html/body/div[@id='wrapper']/div[@id='content']/div[@class='grid-16-8 clearfix']/div[@class='article']/div[@class='related-info']/div[@id='link-report']/span";		
		String smallImg = new Xpath(contentString, imgXpath).getResult();
		String detailNote = new Xpath(contentString,xpathString).getFilterHtmlResult();
		String commentString = new Xpath(contentString, commentXpath).getFilterHtmlResult();
		String commentNumString = new Xpath(contentString, commentNumXpath).getFilterHtmlResult();
		String briefString = new Xpath(contentString,briefXpath).getFilterHtmlResult();
		
		detailNote = detailNote.replaceAll("\n", "|");
		System.out.println(urlString);
		//detailNote = detailNote.replaceAll("||", "$$");
		resultList.add(urlString+"$$"+detailNote+"$$img:"+smallImg+"$$"+"comment:"+commentString+"$$comment_num:"+commentNumString+"$$"+"brief:"+briefString);		
		return resultList;
	}

	@Override
	public List<String> parser4(String contentString,String urlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser5(String contentString,String urlString) {
		// TODO Auto-generated method stub
		return null;
	}


	
	
	

}
