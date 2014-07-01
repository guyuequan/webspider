package com.hq.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Evaluator.Id;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;

public class PptvComicRule implements ParserRule {

	private static int count = 0;
	@Override
	public List<String> parser1(String contentString, String inputString)
			throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
		// TODO Auto-generated method stub
		ArrayList<String> resultList = new ArrayList<String>();
		JSONObject dataObject = new JSONObject(contentString);
		JSONArray dataArray = dataObject.getJSONObject("data").getJSONArray("channel_list");
		
		JSONObject cataObject = dataObject.getJSONObject("data").getJSONObject("m_sort").getJSONObject("subcatalog");
		JSONArray tmpArray = cataObject.names();
		Map<String, String> id_cate = new HashMap<String, String>();
		if(tmpArray!=null){
			for (int j = 0; j < tmpArray.length(); j++) {
				JSONObject subcataObject =cataObject.getJSONObject(tmpArray.getString(j));
				String code = subcataObject.getString("id");
				String catTitle=subcataObject.getString("title");
				//System.out.println(code+"-->"+catTitle);
				id_cate.put(code, catTitle);
			}
		}
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject myObject = dataArray.getJSONObject(i);
			JSONObject basicObject = myObject.getJSONObject("basic");
			//title
			String title = basicObject.getString("title");
			//System.out.println("[INFO] "+title);
			//duration
			int duration  = 0;
			duration = basicObject.getInt("durationSeconds")/60;
			//play_count
			int play_count = 0;
			play_count = basicObject.getInt("views_total");
			//state
			String state = "完结";
			String stateString = basicObject.getString("videoStatus");
			if(stateString.equalsIgnoreCase("4"))
				state = "连载";
			//play_num
			int play_num = 0;
			String tmpString = myObject.getJSONObject("ikan").getString("epg_videoStatusContents");
			if(!tmpString.equalsIgnoreCase("")){
				try {
					play_num=Integer.valueOf(tmpString);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			//play
			String play_url = myObject.getString("target_url");
			JSONArray playArray = new JSONArray();
			JSONObject playObject = new JSONObject();
			playObject.put("play_source","pptv");
			playObject.put("play_url",play_url );
			playObject.put("play_count",play_num);
			playArray.put(playObject);
			
			//img
			String img_url = myObject.getString("img_url");
			JSONArray imgArray = new JSONArray();
			JSONObject imgObject = new JSONObject();
			imgObject.put("source_path",img_url);
			imgObject.put("mode","small");
			imgArray.put(imgObject);
			
			
			JSONObject infoObject = myObject.getJSONObject("info");

			//description
			String description = infoObject.getString("extendDescription");
			//country
			String country = infoObject.getString("areas");
			//cast
			JSONArray castArray = new JSONArray();
		
			if(!infoObject.getString("actors").equalsIgnoreCase("")){
				tmpArray = infoObject.getJSONArray("actors");
				for (int j = 0; j < tmpArray.length(); j++) {
					castArray.put(tmpArray.getJSONObject(j).getString("name"));
				}
			}
			//director
			String director="";
			if(!infoObject.getString("directors").equalsIgnoreCase(""))
				director = infoObject.getJSONArray("directors").getJSONObject(0).getString("name");
			//ratings
			float ratings_score = myObject.getJSONObject("dynamic").getLong("score");
			JSONArray ratingsArray = new JSONArray();
			JSONObject ratingsObject = new JSONObject();
			ratingsObject.put("source_site","pptv");
			ratingsObject.put("ratings_score",ratings_score);
			ratingsObject.put("ratings_num",0);
			ratingsArray.put(ratingsObject);
			
			//source_url
			JSONArray sourceUrlArray = new JSONArray();
			sourceUrlArray.put(inputString);
			//category
			JSONArray categoryArray = new JSONArray();
			String cateString = myObject.getJSONObject("ikan").getString("epg_cataIds");
			if(!cateString.equalsIgnoreCase("")){
				String tmp[] = cateString.split(",");
				for (String string : tmp) {
					categoryArray.put(id_cate.get(string));
				}
			}
			
			title = title.trim();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("category", categoryArray);
			jsonObj.put("play", playArray);
			jsonObj.put("source_url",sourceUrlArray);
			jsonObj.put("director", director);
			jsonObj.put("title", title);
			jsonObj.put("img", imgArray);
			jsonObj.put("play_num", play_num);
			jsonObj.put("description", description);
			jsonObj.put("cast", castArray);
			jsonObj.put("state", state);
			jsonObj.put("duration", duration);
			//jsonObj.put("publish_time", publish_time);
			jsonObj.put("ratings", ratingsArray);
			jsonObj.put("country",country);
			jsonObj.put("play_count", play_count);
			jsonObj.put("timestamp", nowTime);
			
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
