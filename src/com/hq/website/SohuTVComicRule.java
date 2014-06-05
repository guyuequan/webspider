package com.hq.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class SohuTVComicRule implements ParserRule {

	@Override
	public List<String> parser1(String contentString, String inputString) {
		List<String>  resultList = new ArrayList<String>();
//		int itemsNum = 20;
		for (int i=1; i<=4; i++) {
			for(int j=1; j<=5; j++){
			String detailXpath = "/html/body/div[@class='wrapper']/div[@id='content']/div[@class='right']/div[@class='column picList']/div[@class='column-bd clear']/ul[@class='cfix']["+i+"]/li[@class='clear']["+j+"]/div[@class='show-pic']/a/@href";
			String imgXpath = "/html/body/div[@class='wrapper']/div[@id='content']/div[@class='right']/div[@class='column picList']/div[@class='column-bd clear']/ul[@class='cfix']["+i+"]/li[@class='clear']["+j+"]/div[@class='show-pic']/a[@class='pic']/img/@src";

			String detailUrl = new Xpath(contentString, detailXpath).getResult();
			String img_Url = new Xpath(contentString, imgXpath).getResult();
			
			String result = detailUrl + SpiderConfig.SPLIT_STRING
							+ img_Url;
//			System.out.println(img_Url);
			resultList.add(result);			
		}
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) throws JSONException {
		List<String> resultList = new ArrayList<String>();
		
		String titleXpath = "//body/div[@id='contentA']/div[@class='right']/div[@class='blockRA bord clear']/h2/span";
		String authorXpath = "/html/body/div[@id='contentA']/div[@class='right']/div[@class='blockRA bord clear']/div[@class='cont']/p[1]/a";
		String beforeCastXpath = "/html/body/div[@id='contentA']/div[@class='right']/div[@class='blockRA bord clear']";
		String playCountXpath ="/html/body/div[@id='contentA']/div[@class='left']/div[@id='blockA']/div[@id='allist']/div[@class='d1 clear']/div[@class='l']";
		String playCountString = new Xpath(contentString,playCountXpath).getFilterHtmlResult().trim();
		System.out.println("*************");
		System.out.println(playCountString);
		System.out.println("+++++++++++++");
		//int playCount = playCountString.split("\n").length;
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult().trim();
		
		String author = new Xpath(contentString, authorXpath).getFilterHtmlResult().trim();
		JSONArray authorArray = new JSONArray();
		authorArray.put(author);
		
		
		JSONArray cast = new JSONArray();
		String beforeCastString = new Xpath(contentString, beforeCastXpath).getResult();
		int start = 0; 
		int end = 0;
		start = beforeCastString.indexOf("声优");
		if(start != -1) {
			end = beforeCastString.indexOf("</p>", start);
			String[] castStrings = beforeCastString.substring(start, end).split("</a>");
			for (String str : castStrings) {
				int s = str.length()-1;
				while(s>=0 && str.charAt(s)!='>') s--;
				if(s>=0) cast.put(str.substring(s+1));
			}
		}
		
		start = 0; end = 0;
		start = contentString.indexOf("<div class=wz>") + new String("<div class=wz>").length();
		end = contentString.indexOf("</div>", start);
		String description = contentString.substring(start, end);
		
		JSONArray play_url = new JSONArray();
		JSONObject playObject = new JSONObject();
		playObject.put("play_url",inputString.split(SpiderConfig.SPLIT_STRING)[0]);
		playObject.put("play_source","sohuTV");
		playObject.put("play_count", -1);
		play_url.put(playObject);
		
		JSONArray img_url = new JSONArray();
		JSONObject imgObject = new JSONObject();
		imgObject.put("source_path", inputString.split(SpiderConfig.SPLIT_STRING)[1]);
		imgObject.put("mode", "small");
		img_url.put(imgObject);
		
		JSONArray play_source = new JSONArray();
		play_source.put(inputString.split(SpiderConfig.SPLIT_STRING)[0]);
		
		start = contentString.indexOf("年份");
		while(contentString.charAt(start) != '>') start++;
		start ++;
		end = start + 1;
		while(contentString.charAt(end) != '<') end++;
		String publish_time = contentString.substring(start, end).trim();
		publish_time = publish_time.replaceAll("�?", "")+"01-01";
		JSONArray category = new JSONArray();
		start = contentString.indexOf("类型�?");
		end = contentString.indexOf("</p>", start);
		String[] categoryStirngs = contentString.substring(start, end).split("</a>");
		
		for (String str : categoryStirngs) {
			int s = str.length()-1;
			while(s>=0 && str.charAt(s)!='>') s--;
			if(s>=0) category.put(str.substring(s+1));
		}
		
		
//		System.out.println(play_url);

		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 String nowTime = df.format(new Date());
		JSONArray timeArray = new JSONArray();
		timeArray.put(nowTime);
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("title", title);
			jsonObj.put("author", authorArray);
			jsonObj.put("cast", cast);
			jsonObj.put("description", description);
			jsonObj.put("play", play_url);
			jsonObj.put("source_url", play_source);
			jsonObj.put("publish_time", publish_time);
			jsonObj.put("category", category);
			jsonObj.put("img", img_url);
			jsonObj.put("timestramp",timeArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		resultList.add(jsonObj.toString());
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
