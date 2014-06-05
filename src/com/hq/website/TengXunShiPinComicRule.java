package com.hq.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import javax.xml.transform.Templates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class TengXunShiPinComicRule implements ParserRule {
	
	public int getNumber(String source,String start,String end){
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(start+"\\s*([0-9]{1,20})\\s*"+end);
		if(source!=null){
			Matcher m = p.matcher(source); 
			if(m.find()){
				return Integer.valueOf(m.group(1));
			}
		}	
		return 0;
		
	}

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
	public List<String> parser2(String contentString, String inputString) throws JSONException {
		List<String> resultList = new ArrayList<String>();
		String titleXpath = "//body//div[@class='video_title']/strong/a";
		String currentStateXpath = "//body//span[@class='video_current_state']/span[@class='current_state']";
		String totalStateXpath = "//body//span[@class='video_current_state']/span[@class='total']";
		String imgUrlXpath = "//body//a[@class='video_poster figure']/img/@src";
		String descriptionXpath = "//body//span[@class='desc' and @itemprop='description']";
		String playNumXpath = "//body//div[@class='mod_album_titlist_title']/ul/";
		String playNumXpath2 ="//body//div[@class='mod_album_notitlist_lists _videolist']/";
		int play_num = 0;
		String playNumString  = new Xpath(contentString, playNumXpath).getFilterHtmlResult();
		
		int all_play_num = 0;
		if(playNumString!=null){
			playNumString  = playNumString.trim();
			String tmp[] = playNumString.split("-");
			play_num =Integer.valueOf(tmp[tmp.length-1].trim());
		}else{
			String playNumString2 = new Xpath(contentString, playNumXpath2).getFilterHtmlResult();
			if(playNumString2!=null){
				playNumString2 = playNumString2.trim();
				String tmp[] = playNumString2.split("\n");
				play_num =Integer.valueOf(tmp[tmp.length-1].trim());
			}
			
		}

		
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult();
		String currentState = new Xpath(contentString, currentStateXpath).getFilterHtmlResult();
		String totalState = new Xpath(contentString, totalStateXpath).getFilterHtmlResult();
		String imgUrl = new Xpath(contentString, imgUrlXpath).getResult();
		String allDescription = new Xpath(contentString, descriptionXpath).getResult();
		
		String mode = "完结";
		if(currentState!=null&&currentState.startsWith("�?"))
			all_play_num = getNumber(currentState, "�?","�?");
		else if(currentState!=null&&currentState.startsWith("更新")){
			mode = "连载";
			all_play_num = getNumber(currentState, "更新�?","�?");
		}
		
		if(play_num ==0)
			play_num = all_play_num;
		
		String description = null;
		if (allDescription != null && allDescription.contains("★�?�故事简介�??")) {
			int storyStartIndex = allDescription.indexOf("★�?�故事简介�??") + new String("★�?�故事简介�??").length();
			int storyEndIndex = allDescription.indexOf("�?", storyStartIndex);
			if (storyEndIndex == -1) 
				storyEndIndex = allDescription.length();
			description = allDescription.substring(storyStartIndex, storyEndIndex).replaceAll("<br />", "\n").replaceAll("\"", " ").replaceAll("'", " ").replaceAll("�?", " ").replaceAll("�?", " ").trim();
		}
		
		String castString = null;
		JSONArray castJA = new JSONArray();
		if (allDescription != null && allDescription.contains("★�?�CAST�?")) {
			int castStartIndex = allDescription.indexOf("★�?�CAST�?") + new String("★�?�CAST�?").length();
			int castEndIndex = allDescription.length();
			castString = allDescription.substring(castStartIndex, castEndIndex).replace("<br />", "\n").replaceAll(" </span>", "").replaceAll("�?", ".").replaceAll("·", ".").trim();
			String[] strings = castString.split("\n");
			for(String str : strings){
				castJA.put(str);
			}
			
		}
		
		String playUrl = inputString.split(SpiderConfig.SPLIT_STRING)[0];
		JSONArray playUrlJSONArray = new JSONArray();
		JSONObject playObject = new JSONObject();
		playObject.put("play_url", playUrl);
		playObject.put("play_count", play_num);
		playObject.put("play_source","tengxunshipin");
		playUrlJSONArray.put(playObject);
		
		JSONArray imgUrlJSONArray = new JSONArray();
		JSONObject imgObject = new JSONObject();
		imgObject.put("source_path", imgUrl);
		imgObject.put("mode", "small");
		imgUrlJSONArray.put(imgObject);
		
		String director = inputString.split(SpiderConfig.SPLIT_STRING)[1];
		
		JSONArray play_source = new JSONArray();
		play_source.put("腾讯视频");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
		JSONObject jsonObj = new JSONObject();
		
		
		
		try {
			jsonObj.put("play", playUrlJSONArray);
			jsonObj.put("play_num", play_num);//
			jsonObj.put("play_source", play_source);
			jsonObj.put("director", director);
			jsonObj.put("title", title);
			jsonObj.put("img", imgUrlJSONArray);
			jsonObj.put("all_play_num", all_play_num);
			jsonObj.put("description", description);
			jsonObj.put("cast", castJA);
			jsonObj.put("state", mode);
			jsonObj.put("timestamp", nowTime);

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
