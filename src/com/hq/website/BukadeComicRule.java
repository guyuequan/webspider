package com.hq.website;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class BukadeComicRule implements ParserRule {

	@Override
	public List<String> parser1(String contentString, String inputString) {
		List<String>  resultList = new ArrayList<String>();
		int itemsNum = 17;
//		if(inputString.endsWith("list_314.html")) itemsNum = 3;
		/**
		 * 注意：这个网站解析有问题，前两个始终是null，所以从�?3个开始解析，�?15个�??
		 * �?后一页没有这么多，所以会抛异常，属于正常情况�?
		 */
		for (int i=3; i<=itemsNum; i++) {
			
			String detailXpath = "//body//div[@class='k_list']["+i+"]/dl/dt/a/@href";
			
			String detailUrl = new Xpath(contentString, detailXpath).getResult();
			
			String result = detailUrl;
			resultList.add(result);			
		}
		
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) {
		List<String> resultList = new ArrayList<String>();
		
		String play_count = null;
		String play_num = null;
		String titleXpath 			= "//body/div[@id='box']/div[@id='main']/div[@id='c_con']/div[@class='cartoon']/ul/li[1]/h1";
		String itsTotalStateXpath	= "//body/div[@id='box']/div[@id='main']/div[@id='c_con']/div[@class='cartoon']/ul/li[3]";
		String stateXpath 			= "//body/div[@id='box']/div[@id='main']/div[@id='c_con']/div[@class='cartoon']/ul/li[4]";
		String authorXpath			= "//body/div[@id='box']/div[@id='main']/div[@id='c_con']/div[@class='cartoon']/ul/li[5]/a";
		String modeXpath			= "//body/div[@id='box']/div[@id='main']/div[@id='c_con']/div[@class='cartoon']/ul/li[6]";
		String imgXpath				= "//body/div[@id='box']/div[@id='main']/div[@id='c_con']/div[@class='cartoon']/img[@id='cartoonPic']/@src";
		
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult();
		String itsTotalState = new Xpath(contentString, itsTotalStateXpath).getFilterHtmlResult();
		String state = new Xpath(contentString, stateXpath).getFilterHtmlResult();
		String author = new Xpath(contentString, authorXpath).getFilterHtmlResult();
		String mode	= new Xpath(contentString, modeXpath).getFilterHtmlResult();
		mode = mode.substring(mode.indexOf("�?")+1);
		String imgUrl = new Xpath(contentString, imgXpath).getResult();
		
		String itsTotalState2 = "";//提取“全集：xx话�?�中的数�?
		for(char c : itsTotalState.toCharArray()){
			if('0'<=c && c<='9')
				itsTotalState2 += c;
		}
		
		if (state.contains("完结")){//如果是完结，则集数是总集数；如果是连载，则集数是当前集数�?
			state = "完结";
			play_num = itsTotalState2;
		} else 
			if (state.contains("连载")){
				state = "连载";
				play_count = itsTotalState2;
		} else {
			System.err.println("无法识别：state:="+state);
		}
		
		JSONArray img_url = new JSONArray();
		img_url.put(imgUrl);
		JSONObject resultJO = new JSONObject();
		try {
			resultJO.put("title", title);
			resultJO.put("state", state);
			if (play_num != null) resultJO.put("play_num", play_num);
			if (play_count != null) resultJO.put("play_count", play_count);
			resultJO.put("author", author);
			resultJO.put("mode", mode);
			resultJO.put("img_url", img_url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		resultList.add(resultJO.toString());
		return resultList;
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
