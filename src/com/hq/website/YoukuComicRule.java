package com.hq.website;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class YoukuComicRule implements ParserRule {
	private static int count = 0;

	@Override
	public List<String> parser1(String contentString, String inputString) {
		List<String>  resultList = new ArrayList<String>();
		int itemsNum = 42;
		for (int i=1; i<=itemsNum; i++) {
			String detailXpath = "//body//div[@class='yk-row yk-v-80']/div[@class='yk-col3']["+i+"]//div[@class='p-link']/a/@href";
			String detailUrl = new Xpath(contentString,detailXpath).getResult();
			String result = detailUrl;
			resultList.add(result);			
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) throws JSONException {
		// title
		String titleXpath = "//body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@id='title_wrap']/div[@id='title']/div[@class='base']/h1[@class='title']/span[@class='name']";
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult();

		// mode
		String modeXpath = "//body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@id='title_wrap']/div[@id='title']/div[@class='base']/h1[@class='title']/span[@class='edition']";
		String mode = new Xpath(contentString, modeXpath).getFilterHtmlResult();

		// ratings_score
//		String ratings_scoreXpath = "/html/body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@class='left']/div[@id='showInfo_wrap']/div[@id='showInfo']/div[@class='showInfo poster_w']/ul[@class='baseinfo']/li[@class='row1 rate']/span[@class='rating_dp']/em[@class='num']";
//		String ratings_score = new Xpath(contentString, ratings_scoreXpath).getFilterHtmlResult();
		
		// alias
		JSONArray aliasJA = new JSONArray();
		String aliasXpath = "/html/body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@class='left']/div[@id='showInfo_wrap']/div[@id='showInfo']/div[@class='showInfo poster_w']/ul[@class='baseinfo']/li[@class='row1 alias']";
		String aliasString = new Xpath(contentString, aliasXpath).getResult();
		if (aliasString != null) {
			int s = aliasString.indexOf("</label>") + "</label>".length();
			int t = aliasString.indexOf("</li>", s);
			aliasString = aliasString.substring(s,t).trim();
			String[] aliasStrings = aliasString.split("/");
			for (int i=0; i<aliasStrings.length; i++) {
				String aliasStr = aliasStrings[i].trim();
				if (aliasStr.length() > 0) {
					JSONObject aliasJO = new JSONObject();
					aliasJO.put("name", aliasStr);
					aliasJA.put(aliasJO);
				}
			}
		}

		// publish_time
		String publish_timeXpath = "//body//span[@class='pub'][1]";
		String publish_time = new Xpath(contentString, publish_timeXpath).getFilterHtmlResult();
		if(publish_time != null){
			int i=0;
			for(; i<publish_time.length(); i++ )
				if ('0'<publish_time.charAt(i) && publish_time.charAt(i)<'9')
					break;
			publish_time = publish_time.substring(i, publish_time.length());
		}

		// category
		JSONArray categoryJA = new JSONArray();
		String categoryXpath = "//body//span[@class='type'][2]";
		String categoryString = new Xpath(contentString, categoryXpath).getFilterHtmlResult();
		if (categoryString != null) {
			String[] categoryStrings = categoryString.split(" ");
			for(int j=0; j<categoryStrings.length; j++) {
				if((!categoryStrings[j].equals("")) && (!categoryStrings[j].equals("类型:"))) { //leixing
					categoryJA.put(categoryStrings[j]);
				}
			}
		}
		
		// cast
		JSONArray castJA = new JSONArray();
		int start, end = 0;
		start = contentString.indexOf("<label>声优:</label>");
		if (start != -1) {
			start = start + new String("<label>声优:</label>").length();
			end = contentString.indexOf("</span>", start);
			String[] castStrings = contentString.substring(start, end).split("</a>");
			for(String singleCast : castStrings){
				int castNameStart = singleCast.length()-1;
				while(castNameStart>=0 && singleCast.charAt(castNameStart) != '>'){
					castNameStart -- ;
				}
				if(castNameStart > 0){
					castJA.put(singleCast.substring(castNameStart+1));
				}
			}
		}
		
		// state && play_num && all_play_num
		int play_num = 0, all_play_num = 0;
		String state = null;
		start = 0; end = 0;
		start = contentString.indexOf("<div class=\"basenotice\">");
		start = start + new String("<div class=\"basenotice\">").length();
		end = contentString.indexOf("<span", start);
		if (start != -1 && end != -1) {
			String playStateString = contentString.substring(start, end);
			String playState = "";
			for(char c : playStateString.toCharArray()){
				if('0'<=c && c<='9')
					playState = playState + c;
			}
			if (!playState.equals("")) {
				if(playStateString.contains("共")){ //gong
					state = "完结";
					all_play_num = Integer.valueOf(playState);
				}
				else{
					state = "连载";
					play_num = Integer.valueOf(playState);
				}
			}
		}
		
		// play
		JSONArray playJA = new JSONArray();
		JSONObject playJO = new JSONObject();
		playJO.put("play_url", inputString);
		playJO.put("play_count", play_num==0?all_play_num:play_num);
		playJO.put("play_source", "youku");
		playJA.put(playJO);
		
		// source_url
		JSONArray source_urlJA = new JSONArray();
		source_urlJA.put(inputString);
		
		// img
		JSONArray imgJA = new JSONArray();
		String imgUrlXpath = "//body//li[@class='thumb']/img/@src";
		String imgUrl = new Xpath(contentString, imgUrlXpath).getResult();
		JSONObject imgJO = new JSONObject();
		imgJO.put("source_path", imgUrl);
		imgJO.put("mode", "middle");
		imgJA.put(imgJO);
		
		// country
		String country = null;
		String countryXpath = "//span[@class='area']/a";
		country = new Xpath(contentString, countryXpath).getFilterHtmlResult();
		
		//play_count
		long play_count = 0;
		String play_countXpath = "//span[@class='play']";
		String play_countString = new Xpath(contentString, play_countXpath).getFilterHtmlResult();
		if (play_countString != null) {
			play_countString = play_countString.replaceAll("总播放:", "").replaceAll(",", "").trim(); //zongbofang:
			play_count = Long.valueOf(play_countString);
		}
		
		//description
		String description = null;
		String descriptionXpath = "//div[@class='detail']/span/span[2]";
		description = new Xpath(contentString, descriptionXpath).getFilterHtmlResult();
		if (description == null) {
			description = new Xpath(contentString, "//div[@class='detail']/span/span[1]").getFilterHtmlResult();
			if (description == null) {
				description = new Xpath(contentString, "//div[@class='detail']/span").getFilterHtmlResult();
			}
		}
		if (description != null) {
			description = description.trim();
		}
		
		JSONObject jsonObj = new JSONObject(); 
		try {
			jsonObj.put("title", title);
			jsonObj.put("mode", mode);
			jsonObj.put("alias", aliasJA);
			jsonObj.put("publish_time", publish_time);
			jsonObj.put("category", categoryJA);
			jsonObj.put("cast", castJA);
			jsonObj.put("state", state);
			jsonObj.put("play_num", play_num);
			jsonObj.put("all_play_num", all_play_num);
			jsonObj.put("play", playJA);
			jsonObj.put("source_url", source_urlJA);
//			jsonObj.put("ratings_score", ratings_score);
			jsonObj.put("img", imgJA);
			jsonObj.put("country", country);
			jsonObj.put("play_count", play_count);
			jsonObj.put("description", description);
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
