package com.hq.website;

import java.io.UnsupportedEncodingException;
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
	private static int count = 0;
	
	@Override
	public List<String> parser1(String contentString, String inputString) {
		List<String>  resultList = new ArrayList<String>();
		for (int i=1; i<=4; i++) {
			for(int j=1; j<=5; j++){
				String detailXpath = "/html/body/div[@class='wrapper']/div[@id='content']/div[@class='right']/div[@class='column picList']/div[@class='column-bd clear']/ul[@class='cfix']["+i+"]/li[@class='clear']["+j+"]/div[@class='show-pic']/a/@href";
				String detailUrl = new Xpath(contentString, detailXpath).getResult();
				if (detailUrl==null || detailUrl.equals("")) continue;
	
				String imgXpath = "/html/body/div[@class='wrapper']/div[@id='content']/div[@class='right']/div[@class='column picList']/div[@class='column-bd clear']/ul[@class='cfix']["+i+"]/li[@class='clear']["+j+"]/div[@class='show-pic']/a[@class='pic']/img/@src";
				String img_Url = new Xpath(contentString, imgXpath).getResult();
	
				String titleXpath = "/html/body/div[@class='wrapper']/div[@id='content']/div[@class='right']/div[@class='column picList']/div[@class='column-bd clear']/ul[@class='cfix']["+i+"]/li[@class='clear']["+j+"]/div[@class='show-txt']/div[@class='info']/p[@class='tit tit-p']/a";
				String title = new Xpath(contentString, titleXpath).getFilterHtmlResult();
	
				// state && play_num && all_play_num
				String tempXpath = "html/body/div[@class='wrapper']/div[@id='content']/div[@class='right']/div[@class='column picList']/div[@class='column-bd clear']/ul[@class='cfix']["+i+"]/li[@class='clear']["+j+"]/div[@class='show-pic']/div[@class='filter']/i";
				String temp = new Xpath(contentString, tempXpath).getFilterHtmlResult();
				
				String result = detailUrl + SpiderConfig.SPLIT_STRING
								+ img_Url + SpiderConfig.SPLIT_STRING
								+ title + SpiderConfig.SPLIT_STRING
								+ temp;
//				System.out.println(result+";i="+i+";j="+j+";");
				resultList.add(result);
			}
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) throws JSONException, UnsupportedEncodingException {
		contentString = new String(contentString.getBytes("iso8859-1"), "GBK");
		
		// title
		String title = inputString.split(SpiderConfig.SPLIT_STRING)[2];

		// author
		JSONArray authorJA = new JSONArray();
		String authorPXpath = "/html/body/div[@id='contentA']/div[@class='right']/div[@class='blockRA bord clear']/div[@class='cont']/p[1]";
		String authorP = new Xpath(contentString, authorPXpath).getFilterHtmlResult();
		if (authorP != null && authorP.contains("原作")) { //yuanzuo
			String authorXpath = "/html/body/div[@id='contentA']/div[@class='right']/div[@class='blockRA bord clear']/div[@class='cont']/p[1]/a";
			String author = new Xpath(contentString, authorXpath).getFilterHtmlResult();
			if (author != null){
				author = author.trim();
				authorJA.put(author);
			}
		}
		
		// cast
		JSONArray castJA = new JSONArray();
		String beforeCastXpath = "/html/body/div[@id='contentA']/div[@class='right']/div[@class='blockRA bord clear']";
		String beforeCastString = new Xpath(contentString, beforeCastXpath).getResult();
		int start = 0; 
		int end = 0;
		if (beforeCastString != null) {
			start = beforeCastString.indexOf("声优"); //shengyou
			if(start != -1) {
				end = beforeCastString.indexOf("</p>", start);
				String[] castStrings = beforeCastString.substring(start, end).split("</a>");
				for (String str : castStrings) {
					int s = str.length()-1;
					while(s>=0 && str.charAt(s)!='>') s--;
					if(s>=0) castJA.put(str.substring(s+1));
				}
			}
		}
		
		// description
		start = 0; end = 0;
		start = contentString.indexOf("<div class=wz>") + new String("<div class=wz>").length();
		end = contentString.indexOf("</div>", start);
		String description = contentString.substring(start, end);
		
		// play_num && all_play_num && state
		String state = null;
		int play_num = 0;
		int all_play_num = 0;
		String playNumString = inputString.split(SpiderConfig.SPLIT_STRING)[3];
		start = 0; end = 0;
		while (playNumString.charAt(start) < '0' || playNumString.charAt(start) > '9') {
			start++;
		}
		end = start;
		while (playNumString.charAt(end) >= '0' && playNumString.charAt(end) <= '9') {
			end++;
		}
		if (playNumString.contains("共")) { //gong
			state = "完结"; //wanjie
			all_play_num = Integer.valueOf(playNumString.substring(start, end));
			play_num = all_play_num;
		}
		else {
			state = "连载"; //lianzai
			play_num = Integer.valueOf(playNumString.substring(start, end));
		}

		// play_count(TBD)
		
		// play
		JSONArray playJA = new JSONArray();
		JSONObject playObject = new JSONObject();
		playObject.put("play_url",inputString.split(SpiderConfig.SPLIT_STRING)[0]);
		playObject.put("play_source","sohuTV");
		playObject.put("play_count", play_num==0?all_play_num:play_num);
		playJA.put(playObject);
		
		// img
		JSONArray imgJA = new JSONArray();
		JSONObject imgObject = new JSONObject();
		imgObject.put("source_path", inputString.split(SpiderConfig.SPLIT_STRING)[1]);
		imgObject.put("mode", "small");
		imgJA.put(imgObject);
		
		// source_url
		JSONArray source_url = new JSONArray();
		source_url.put(inputString.split(SpiderConfig.SPLIT_STRING)[0]);
		
		// publish_time
		String publish_time = "";
		start = contentString.indexOf("年份"); //nianfen
		if (start != -1) {
			while(contentString.charAt(start) != '>') start++;
			start ++;
			end = start + 1;
			while(contentString.charAt(end) != '<') end++;
			publish_time = contentString.substring(start, end).trim();
			publish_time = publish_time.replaceAll("年", "")+"-01-01"; //nian
		}
		
		// category
		JSONArray categoryJA = new JSONArray();
		start = contentString.indexOf("类型："); //leixing + zhongwenmaohao
		end = contentString.indexOf("</p>", start);
		try {
			String[] categoryStirngs = contentString.substring(start, end).split("</a>");
			for (String str : categoryStirngs) {
				int s = str.length()-1;
				while(s>=0 && str.charAt(s)!='>') s--;
				if(s>=0) categoryJA.put(str.substring(s+1));
			}
		} catch (Exception e) {
			// TODO: handle exception
			//can not get category
		}
		
		
		// timestamp
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = df.format(new Date());
		
		JSONObject jsonObj = new JSONObject();
		try {
			title = title.trim();
			jsonObj.put("title", title);
			jsonObj.put("author", authorJA);
			jsonObj.put("cast", castJA);
			jsonObj.put("description", description);
			jsonObj.put("play", playJA);
			jsonObj.put("source_url", source_url);
			jsonObj.put("publish_time", publish_time);
			jsonObj.put("category", categoryJA);
			jsonObj.put("img", imgJA);
			jsonObj.put("timestamp",timestamp);
			jsonObj.put("state", state);
			jsonObj.put("play_num", play_num);
			jsonObj.put("all_play_num", all_play_num);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println("[INFO] " + title + SpiderConfig.SPLIT_STRING+(++count));
		List<String> resultList = new ArrayList<String>();
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
