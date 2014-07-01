package com.hq.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class IqiyiComicRule implements ParserRule{
	private static int count = 0;

	@Override
	public List<String> parser1(String contentString, String inputString) throws Exception {
		List<String>  resultList = new ArrayList<String>();
		int itemsNum = 20;
		for (int i=1; i<=itemsNum; i++) {
			String imgURLXpath = "//body//ul[@class='ulList']/li[" + i + "]//img[1]/@src";
			String detailUrlXpath = "//body//ul[@class='ulList']/li[" + i + "]/a[1]/@href";
			String titleXpath = "//body//ul[@class='ulList']/li[" + i + "]/a[@class='title']";
			String stateXpath = "//body//ul[@class='ulList']/li[" + i + "]/a[1]/span[@class='imgBg1C']";
			
			String imgURL = new Xpath(contentString, imgURLXpath).getResult();
			if (imgURL == null) continue;
			String detailUrl = new Xpath(contentString, detailUrlXpath).getResult();
			String title = new Xpath(contentString, titleXpath).getFilterHtmlResult();
			String stateString = new Xpath(contentString, stateXpath).getFilterHtmlResult();
			String state = null;
			int all_play_num = 0;
			int play_num = 0;
			if (stateString != null) {
				int s = 0;
				while (stateString.charAt(s) < '0' || stateString.charAt(s) > '9') {
					s++;
				}
				int t = s;
				while (stateString.charAt(t) >= '0' && stateString.charAt(t) <= '9') {
					t++;
				}
				if (stateString.trim().startsWith("共")) { //gong
					state = "完结"; //wanjie
					all_play_num = Integer.parseInt(stateString.substring(s, t));
				}
				else {
					state = "连载"; //lianzai
					play_num = Integer.parseInt(stateString.substring(s, t));
				}
			}
//			System.out.println("state:"+state+";all_play_num:"+all_play_num+";play_num:"+play_num);
			String result = detailUrl + SpiderConfig.SPLIT_STRING +
							title + SpiderConfig.SPLIT_STRING +
							imgURL + SpiderConfig.SPLIT_STRING +
							state + SpiderConfig.SPLIT_STRING +
							all_play_num + SpiderConfig.SPLIT_STRING +
							play_num;
			resultList.add(result);
		}
		
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) throws Exception {
		contentString = new String(contentString.getBytes("iso8859-1"),SpiderConfig.DEFAULT_CHARSET);
		
		List<String>  resultList = new ArrayList<String>();
		JSONObject resultJO = new JSONObject();
		
		// source_url
		String[] inputs = inputString.split(SpiderConfig.SPLIT_STRING);
		JSONArray source_urlJA = new JSONArray();
		source_urlJA.put(inputs[0]);
		
		// title
		String title = inputs[1];
		
		// img
		JSONArray imgJA = new JSONArray();
		JSONObject imgJO1 = new JSONObject();
		imgJO1.put("source_path", inputs[2]);
		imgJO1.put("mode", "small");
		imgJA.put(imgJO1);
		
		// state
		String state = inputs[3];
		
		// all_play_num
		int all_play_num = Integer.parseInt(inputs[4]);
		
		// play_num
		int play_num = Integer.parseInt(inputs[5]);
		
		// play
		JSONArray playJA = new JSONArray();
		JSONObject play1 = new JSONObject();
		play1.put("play_url", inputs[0]);
		play1.put("play_count", (play_num != 0) ? play_num : all_play_num);
		play1.put("play_source", "iqiyi");
		playJA.put(play1);
		
		// timestamp
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String timestamp = df.format(new Date());
		
		if (contentString.contains("标签")) { //biaoqian
			// mode && category
			String mode = null;
			JSONArray categoryJA = new JSONArray();
			int s = contentString.indexOf("标签"); //biaoqian
			int t = contentString.indexOf("</p>", s);
			String categoryStrings = contentString.substring(s, t);
			int i = 1;
			boolean isFirst = true;
			String mode_category_baseXpath = "//a[";
			String mode_categoryString = new Xpath(categoryStrings, mode_category_baseXpath+i+"]").getFilterHtmlResult();
			while (mode_categoryString != null) {
				if (mode_categoryString.length() == 0) {
					i++;
					mode_categoryString = new Xpath(categoryStrings, mode_category_baseXpath+i+"]").getFilterHtmlResult();
					continue;
				}
				if (isFirst) {
					mode = mode_categoryString;
					isFirst = false;
				}
				else {
					categoryJA.put(mode_categoryString);
				}
				i++;
				mode_categoryString = new Xpath(categoryStrings, mode_category_baseXpath+i+"]").getFilterHtmlResult();
			}
			
			// play_count
			String play_count = new Xpath(contentString, "//span[@id='playCount']").getFilterHtmlResult();
			
			//description
			String description = new Xpath(contentString, "//div[@id='j-album-more']").getFilterHtmlResult();
			if ( description!=null ) {
				description = description.trim();
				if ( description.endsWith("收起") ) { //shouqi
					int end = description.length() - "收起".length(); //shouqi
					description = description.substring(0, end);
					description = description.trim();
				}
				description = description.replaceAll("　", "");
			}
			
//			System.out.println("description:"+description);
			
			resultJO.put("mode", mode);
			resultJO.put("category", categoryJA);
			resultJO.put("play_count", play_count);
			resultJO.put("description", description);
			
		} else {
			String movieMsgXpath = "//div[@class='movieMsg']";
			String movieMsg = new Xpath(contentString, movieMsgXpath).getResult();
			
			// mode && category
			String mode = null;
			JSONArray categoryJA = new JSONArray();
			int i = 1;
			String mode_category_baseXpath = "//p[1]/a[";
			String mode_categoryString = null;
			try {
				mode_categoryString = new Xpath(movieMsg, mode_category_baseXpath+i+"]").getFilterHtmlResult();
			} catch (Exception e) {
				// TODO: handle exception
			}
			while (mode_categoryString != null) {
				if (mode_categoryString.length() == 0) continue;
				if (1 == i) {
					mode = mode_categoryString;
				}
				else {
					categoryJA.put(mode_categoryString);
				}
				i++;
				mode_categoryString = new Xpath(movieMsg, mode_category_baseXpath+i+"]").getFilterHtmlResult();
			}
			
			// description
			String description = null;
			int s = movieMsg.indexOf("简介"); //jianjie
			s = movieMsg.indexOf("<p", s);
			s = movieMsg.indexOf(">", s);
			s++;
			int t = movieMsg.indexOf("<", s);
			description = movieMsg.substring(s, t);
			if (description != null) {
				description = description.replaceAll("　", "").trim();
			}
			
			// publish_time
			String publish_time = null;
			s = movieMsg.indexOf("发布时间：")+"发布时间：".length(); //fabushijian; fabushijian
			t = movieMsg.indexOf("</p>", s);
			publish_time = movieMsg.substring(s, t).trim();
			if (publish_time.length() == 4) {
				publish_time = publish_time + "-01-01";
			}
			
			// cast && actor
			JSONArray castJA = new JSONArray();
			JSONArray actorJA = new JSONArray();
			String cast_actorXpath = "//div[@class='peos-info']";
			String cast_actorDiv = new Xpath(contentString, cast_actorXpath).getResult();
			if (cast_actorDiv != null) {
				int j = 1;
				String cast_actorString = new Xpath(cast_actorDiv, "//p["+j+"]").getFilterHtmlResult();
				while (cast_actorString != null && cast_actorString.length() > 0) {
					String[] cast_actorStrings = cast_actorString.replaceAll("饰", ":").split(":"); //shi
					if (cast_actorStrings.length > 1) {
						actorJA.put(cast_actorStrings[0].trim());
						castJA.put(cast_actorStrings[1].trim());
					}
					else {
						castJA.put(cast_actorStrings[0].trim());
					}
					j++;
					cast_actorString = new Xpath(cast_actorDiv, "//p["+j+"]").getFilterHtmlResult();
				}
			}
			
			// country
			String country = new Xpath(contentString, "//meta[@itemprop='contentLocation']/@content").getResult();
			
//			System.out.println("country:"+country);
			resultJO.put("mode", mode);
			resultJO.put("category", categoryJA);
			resultJO.put("description", description);
			resultJO.put("publish_time", publish_time);
			resultJO.put("cast", castJA);
			resultJO.put("country", country);
		}
		title =title.trim();
		resultJO.put("source_url", source_urlJA);
		resultJO.put("title", title);
		resultJO.put("img", imgJA);
		resultJO.put("play", playJA);
		resultJO.put("timestamp", timestamp);
		resultJO.put("state", state);
		resultJO.put("all_play_num", all_play_num);
		resultJO.put("play_num", play_num);
		
		System.out.println("[INFO] " + title + SpiderConfig.SPLIT_STRING+(++count));
		resultList.add(resultJO.toString());
		return resultList;
	}

	@Override
	public List<String> parser3(String contentString, String inputString) throws Exception {
		return null;
	}

	@Override
	public List<String> parser4(String contentString, String inputString) throws Exception {
		return null;
	}

	@Override
	public List<String> parser5(String contentString, String inputString) throws Exception {
		return null;
	}
}
