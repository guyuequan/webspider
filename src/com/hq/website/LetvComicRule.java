package com.hq.website;

import java.awt.Dialog.ModalExclusionType;
import java.lang.Thread.State;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Source;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;

public class LetvComicRule implements ParserRule{

	private static int count =0;
	@Override
	public List<String> parser1(String contentString, String inputString)
			throws Exception {
		// TODO Auto-generated method stub
		//System.out.println(contentString);
		ArrayList<String> resultList = new ArrayList<String>();
		
		JSONObject dataObject = new JSONObject(contentString);
		JSONArray dataArray = dataObject.getJSONArray("data_list");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
		
		for (int i = 0; i < dataArray.length(); i++) {
			//for each item
			
			JSONObject myObject = dataArray.getJSONObject(i);
			String title = myObject.getString("name");
			String state = myObject.getInt("isEnd") ==1?"完结":"连载";
			
			//category
			JSONArray categoryArray = new JSONArray();
			String tmp[] = myObject.getString("subCategoryName").split(",");
			for (String string : tmp) {
				categoryArray.put(string);
			}
			
			//play_count
			int play_count=myObject.getInt("playCount");
			//all_play_num
			int all_play_num = 0;
			if(!myObject.getString("episodes").equalsIgnoreCase(""))
				all_play_num = myObject.getInt("episodes");
			//play_num
			int play_num = 0;
			if(!myObject.getString("nowEpisodes").equalsIgnoreCase(""))
				play_num = myObject.getInt("nowEpisodes");
			//play
			JSONArray playArray = new JSONArray();
			tmp =myObject.getString("vids").split(",");
			JSONObject playObject = new JSONObject();
			playObject.put("play_url","http://www.letv.com/ptv/vplay/"+tmp[0]+".html");
			playObject.put("play_source","letv");
			playObject.put("play_count",play_num);
			playArray.put(playObject);
			//country
			String country = myObject.getString("areaName");
			//cast
			JSONArray castArray = new JSONArray();
			JSONObject castObject = myObject.getJSONObject("actor");
			JSONArray tmpArray = castObject.names();
			if(tmpArray!=null){
				JSONArray tmpArray2 = castObject.names();
				for (int j = 0; j < tmpArray2.length(); j++) {
					castArray.put(castObject.getString(tmpArray2.getString(j)));
				}
			}
			//actor
			JSONArray actorArray = new JSONArray();
			tmp = myObject.getString("actorPlay").split(",");
			for (String string : tmp) {
				if(!string.equalsIgnoreCase(""))
					actorArray.put(string);
			}
			//duration
			int duration = myObject.getInt("duration");
			//mode
			String mode = "tv";
			String modeString = myObject.getString("videoTypeName");
			if(modeString == "TV版")
				mode = "tv";
			else if(modeString =="剧场版")
				mode = "movie";
			else if(modeString == "OVA版")
				mode = "ova";
			else {
				mode = "sp";
			}
			
			
			//author
			JSONArray authorArray = new JSONArray();
			JSONObject authorObject = new JSONObject(myObject.getString("directory"));
			if(authorObject!=null&&authorObject.length()>0){
				
				String authorString = authorObject.getString(authorObject.names().get(0).toString());
				authorArray.put(authorString);
			}
			//System.out.println(authorArray);
			//description
			String description = myObject.getString("description");
			//publish_time
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String timeTmp = myObject.getString("releaseDate");
			String publish_time = "0000-00-00";
			if(timeTmp.length()>=10)
				try {
					publish_time= sdf.format(new Date(Integer.valueOf(myObject.getString("releaseDate").substring(0, 10))*1000L));
				} catch (Exception e) {
					// TODO: handle exception
				}
			//img
			JSONArray imgArray = new JSONArray();
			JSONObject imgObject = myObject.getJSONObject("images");
			JSONObject img1 = new JSONObject();
			img1.put("mode", "small");//90-120
			if(imgObject.has("90*120")){
				img1.put("source_path",imgObject.get("90*120"));
				imgArray.put(img1);
			}
			JSONObject img2 = new JSONObject();
			img2.put("mode", "middle");//150-200
			if(imgObject.has("150*200")){
				img2.put("source_path",imgObject.get("150*200"));
				imgArray.put(img2);
			}
			JSONObject img3 = new JSONObject();
			img3.put("mode", "big");//300-400
			if(imgObject.has("300*400")){
				img3.put("source_path",imgObject.get("300*400"));
				imgArray.put(img3);
			}
			
			//source url
			JSONArray sourceUrlArray = new JSONArray();
			sourceUrlArray.put(inputString);
			
			//director  from starring
			JSONObject directorObject = myObject.getJSONObject("starring");
			String director = "";
			if(directorObject!=null&&directorObject.length()>0)
				director = directorObject.getString(directorObject.names().get(0).toString());
			
			//tag
			tmp = myObject.getString("tag").split(" ");
			JSONArray tagArray = new JSONArray();
			if(tmp.length>0){
				for (String string : tmp) {
					if(!string.equalsIgnoreCase(""))
						tagArray.put(string);
				}
			}
			
			//alias
			JSONArray aliasArray = new JSONArray();
			tmp = myObject.getString("otherName").split(" ");
			if(tmp.length>0){
				for (String string : tmp) {
					if(!string.equalsIgnoreCase("")){
						JSONObject tmpObject = new JSONObject();
						tmpObject.put("name", string);
						aliasArray.put(tmpObject);
					}
				}
			}
			//ratings
			JSONArray ratArray = new JSONArray();
			float ratings_score = myObject.getLong("rating");
			JSONObject ratingObject = new JSONObject();
			ratingObject.put("ratings_score", ratings_score);
			ratingObject.put("ratings_num", 0);
			ratingObject.put("source_site", "letv");
			ratArray.put(ratingObject);
			
			title = title.trim();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("category", categoryArray);
			jsonObj.put("play", playArray);
			jsonObj.put("author", authorArray);
			jsonObj.put("source_url",sourceUrlArray);
			jsonObj.put("director", director);
			jsonObj.put("title", title);
			jsonObj.put("img", imgArray);
			jsonObj.put("all_play_num", all_play_num);
			jsonObj.put("play_num", play_num);
			jsonObj.put("description", description);
			jsonObj.put("cast", castArray);
			jsonObj.put("state", state);
			jsonObj.put("tag", tagArray);
			jsonObj.put("alias",aliasArray);
			jsonObj.put("duration", duration);
			jsonObj.put("publish_time", publish_time);
			jsonObj.put("ratings", ratArray);
			jsonObj.put("actor", actorArray);
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
