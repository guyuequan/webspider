package com.hq.parser.website;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class TengXunShiPinComicRule implements ParserRule {

	@Override
	public List<String> parser1(String contentString, String inputString) {
		List<String> resultList = new ArrayList<String>();
		int itemsNum = 10;
		if (inputString.endsWith("60_1_10.html")) itemsNum = 9;
		for (int i=1; i<=itemsNum; i++) {
			String playUrlXpath = "//body//div[@id='content']/div[@class='mod_item']["+i+"]/div[@class='mod_pic']/a/@href";
			String directorXpath = "//body//div[@id='content']/div[@class='mod_item']["+i+"]//li[@class='director']/a";
			
			String playUrl = new Xpath(contentString, playUrlXpath).getResult();
			String director = new Xpath(contentString, directorXpath).getFilterHtmlResult();
			
			resultList.add(playUrl + SpiderConfig.SPLIT_STRING
							+ director);
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) {
		List<String> resultList = new ArrayList<String>();
		String titleXpath = "//body//div[@class='video_title']/strong/a";
		String currentStateXpath = "//body//span[@class='video_current_state']/span[@class='current_state']";
		String totalStateXpath = "//body//span[@class='video_current_state']/span[@class='total']";
		String imgUrlXpath = "//body//a[@class='video_poster figure']/img/@src";
		String descriptionXpath = "//body//span[@class='desc' and @itemprop='description']";
		
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult();
		String currentState = new Xpath(contentString, currentStateXpath).getFilterHtmlResult();
		String totalState = new Xpath(contentString, totalStateXpath).getFilterHtmlResult();
		String imgUrl = new Xpath(contentString, imgUrlXpath).getResult();
		String allDescription = new Xpath(contentString, descriptionXpath).getResult();
		String description = null;
		if (allDescription != null && allDescription.contains("★【故事简介】")) {
			int storyStartIndex = allDescription.indexOf("★【故事简介】") + new String("★【故事简介】").length();
			int storyEndIndex = allDescription.indexOf("★", storyStartIndex);
			if (storyEndIndex == -1) 
				storyEndIndex = allDescription.length();
			description = allDescription.substring(storyStartIndex, storyEndIndex).replaceAll("<br />", "\n").replaceAll("\"", " ").replaceAll("'", " ").replaceAll("“", " ").replaceAll("”", " ").trim();
		}
		
		String castString = null;
		JSONArray castJA = new JSONArray();
		if (allDescription != null && allDescription.contains("★【CAST】")) {
			int castStartIndex = allDescription.indexOf("★【CAST】") + new String("★【CAST】").length();
			int castEndIndex = allDescription.length();
			castString = allDescription.substring(castStartIndex, castEndIndex).replace("<br />", "\n").replaceAll(" </span>", "").replaceAll("・", ".").replaceAll("·", ".").trim();
			String[] strings = castString.split("\n");
			for(String str : strings){
				castJA.put(str);
			}
			
		}
		
		String playUrl = inputString.split(SpiderConfig.SPLIT_STRING)[0];
		JSONArray playUrlJSONArray = new JSONArray();
		playUrlJSONArray.put(playUrl);
		
		JSONArray imgUrlJSONArray = new JSONArray();
		imgUrlJSONArray.put(imgUrl);
		
		String director = inputString.split(SpiderConfig.SPLIT_STRING)[1];
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("play_url", playUrlJSONArray);
			jsonObj.put("play_source", "腾讯视频");
			jsonObj.put("director", director);
			jsonObj.put("title", title);
			jsonObj.put("img_url", imgUrlJSONArray);
			jsonObj.put("play_count", currentState);
			jsonObj.put("play_num", totalState);
			jsonObj.put("description", description);
			jsonObj.put("cast", castJA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
//		System.out.println("playUrl:"+inputString+";description: "+ description);
		resultList.add(jsonObj.toString());
		return resultList;
	}

	@Override
	public List<String> parser3(String contentString, String inputString) {
		return null;
	}

	@Override
	public List<String> parser4(String contentString, String inputString) {
		return null;
	}

	@Override
	public List<String> parser5(String contentString, String inputString) {
		return null;
	}

}
