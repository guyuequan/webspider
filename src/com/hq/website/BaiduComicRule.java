package com.hq.website;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.Httphandle;
import com.hq.spider.util.SpiderConfig;

public class BaiduComicRule implements ParserRule {

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
		String baseUrl = "http://v.baidu.com/comic_intro/?dtype=comicPlayUrl&service=json&frp=browse&site=default&id=";
		ArrayList<String> resultList = new ArrayList<String>();
		JSONObject dataObject = new JSONObject(contentString);
		JSONArray dataArray = dataObject.getJSONObject("videoshow").getJSONArray("videos");
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject myObject = dataArray.getJSONObject(i);
			String title = myObject.getString("title");
			String id = myObject.getString("id");
			String urlString = baseUrl+id;
		
			//season
			int season = 0;
			if(!myObject.isNull("season")){
				try {
					season=myObject.getInt("season");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			//play_num state
			String state = "完结";
			int all_play_num = 0;
			int play_num = 0;
			String update = myObject.getString("update");
			if(update.contains("更新至")){
				state = "连载";
				play_num = getNumber(update, "更新至","话");		
			}else{
				all_play_num = getNumber(update,"全", "话");
				play_num = all_play_num;
			}
			JSONArray playArray = new JSONArray();
			//play
			try {
				Thread.sleep(10);
				Httphandle httphandle  = new Httphandle();
				String contentString2 = httphandle.get(urlString);
				JSONArray playArray1 = new JSONArray(contentString2);
				JSONArray episodeArray = playArray1.getJSONObject(0).getJSONArray("episodes");
				String playUrl =episodeArray.getJSONObject(0).getString("url");
				String playSource = episodeArray.getJSONObject(0).getString("site_url");
				if(state.equalsIgnoreCase("完结")){
					playUrl = episodeArray.getJSONObject(episodeArray.length()-1).getString("url");
					playSource =  episodeArray.getJSONObject(episodeArray.length()-1).getString("site_url");
				}
				playSource = playSource.replaceAll(".com", "");
				JSONObject playObject = new JSONObject();
				playObject.put("play_url", playUrl);
				playObject.put("play_source", playSource);
				playObject.put("play_count", play_num);
				playArray.put(playObject);	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			///title
			if(season !=0)
				title += " 第"+season+"部";
			if(title.contains("TV版"))
				title = title.replaceAll("TV版","");
			title = title.trim();
			
			//img
			JSONArray imgArray = new JSONArray();
			JSONObject tmpObject1 = new JSONObject();
			tmpObject1.put("source_path", myObject.getString("imgh_url"));
			tmpObject1.put("mode", "small");
			imgArray.put(tmpObject1);
			JSONObject tmpObject2 = new JSONObject();
			tmpObject2.put("source_path", myObject.getString("imgv_url"));
			tmpObject2.put("mode", "middle");
			imgArray.put(tmpObject2);
			//publish_time
			String publish_time = myObject.getString("date")+"-01-01";
			String country =myObject.getJSONArray("area").getJSONObject(0).getString("name");
			if(country.equalsIgnoreCase("国产"))
				country = "中国大陆";
			//category
			JSONArray cateArray = new JSONArray();
			JSONArray tmpArray = myObject.getJSONArray("type");
			for (int j = 0; j < tmpArray.length(); j++) {
				cateArray.put(tmpArray.getJSONObject(j).getString("name"));
			}
			//description
			String description = myObject.getString("intro");
			//source_url
			JSONArray sourceUrlArray = new JSONArray();
			sourceUrlArray.put(inputString);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("category", cateArray);
			if(playArray!=null)
				jsonObj.put("play", playArray);
			jsonObj.put("source_url",sourceUrlArray);
			jsonObj.put("title", title);
			jsonObj.put("img", imgArray);
			jsonObj.put("all_play_num", all_play_num);
			jsonObj.put("play_num", play_num);
			jsonObj.put("description", description);
			jsonObj.put("state", state);
			jsonObj.put("publish_time", publish_time);
			jsonObj.put("country",country);		
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
