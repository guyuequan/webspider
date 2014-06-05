package com.hq.parser.website;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.Xpath;

public class WeiboRankParser implements ParserRule {

	@Override
	public List<String> parser1(String contentString, String inputString) {
		// TODO Auto-generated method stub
		List<String> resultList = new ArrayList<String>();

		for (int i = 2; i < 22; i++) {
			
			String wordXpath = "/html/body[@class='B_billboard']/div[@class='b_bodyBg']/div[@class='W_main']/div[@class='W_main_bg clearfix']" +
					"/div[@class='W_main_2col_r']/div[@id='pl_top_topicList']/table[@class='box_Show_z box_zs']/tbody/tr["+i+"]/td[2]/span[@class='zw_topic']/a";
			String resString = new Xpath(contentString, wordXpath).getFilterHtmlResult();
			resultList.add(resString);
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) {
		
		//System.out.println(contentString);
		
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser3(String contentString, String inputString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser4(String contentString, String inputString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser5(String contentString, String inputString) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
