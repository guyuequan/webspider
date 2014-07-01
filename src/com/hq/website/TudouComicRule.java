package com.hq.website;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;

public class TudouComicRule implements ParserRule {
	
	private static int count = 0;
	public int getNumber(String source,String start,String end){
		try {
			java.util.regex.Pattern p = java.util.regex.Pattern.compile(start+"\\s*([0-9]{1,20})\\s*"+end);
			if(source!=null){
				Matcher m = p.matcher(source); 
				if(m.find()){
					return Integer.valueOf(m.group(1));
				}
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	@Override
	public List<String> parser1(String contentString, String inputString)
			throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
		// TODO Auto-generated method stub
		ArrayList<String> resultList = new ArrayList<String>();
		String tmpString = new String(contentString.getBytes("iso8859-1"),"utf-8");
		JSONObject dataObject = new JSONObject(tmpString);
		JSONArray dataArray = dataObject.getJSONArray("data");
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject myObject = dataArray.getJSONObject(i);
			String title = myObject.getString("albumName");
			String updateString = myObject.getString("update");
			String state = "完结";
			int play_num = 0;
			int all_play_num = 0;
			if(updateString.contains("更新")){
				state = "连载";
				play_num = getNumber(updateString, "更新", "集");
			}else{
				state = "完结";
				all_play_num = getNumber(updateString,"全","集");
				play_num = all_play_num;
			}
			//play
			JSONArray playArray = new JSONArray();
			JSONObject playObject = new JSONObject();
			playObject.put("play_source", "tudou");
			playObject.put("play_count", play_num);
			playObject.put("play_url", myObject.getString("playUrl"));
			playArray.put(playObject);
			
			//img
			JSONArray imgArray = new JSONArray();
			JSONObject imgObject = new JSONObject();
			imgObject.put("source_path", myObject.getString("albumPicUrl"));
			imgObject.put("mode", "small");
			JSONObject imgObject1 = new JSONObject();
			imgObject1.put("source_path", myObject.getString("albumBigPicUrl"));
			imgObject1.put("mode", "big");
			imgArray.put(imgObject);
			imgArray.put(imgObject1);
			
			//description
			String description = "";
			description = myObject.getString("albumShortDesc");
			/////
			JSONArray sourceUrlArray = new JSONArray();
			sourceUrlArray.put(inputString);
			//mode
			String mode = "tv";
			if(play_num == all_play_num &&play_num == 1)
				mode = "movie";
			
			title = title.trim();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("play", playArray);
			jsonObj.put("source_url",sourceUrlArray);
			jsonObj.put("title", title);
			jsonObj.put("img", imgArray);
			jsonObj.put("play_num", play_num);
			jsonObj.put("description", description);
			jsonObj.put("state", state);
			jsonObj.put("all_play_num", all_play_num);
			//jsonObj.put("publish_time", publish_time);
			jsonObj.put("timestamp", nowTime);
			jsonObj.put("mode", mode);
			resultList.add(jsonObj.toString());
			System.out.println("[INFO] " + title + SpiderConfig.SPLIT_STRING+(++count));
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser3(String contentString, String inputString)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser4(String contentString, String inputString)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser5(String contentString, String inputString)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
