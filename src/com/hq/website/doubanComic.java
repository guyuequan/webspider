package com.hq.website;

import java.awt.SystemTray;
import java.awt.Dialog.ModalExclusionType;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale.Category;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

/**
 * the parser for doubanComic
 * @author huqian.hq
 *
 */
public class doubanComic implements ParserRule {
	
	private static int count = 0;
	@Override
	public List<String> parser1(String contentString,String urlString) {
		List<String>  resultList = new ArrayList<String>();
		String doubanApi = "https://api.douban.com/v2/movie/";
		int offset  =20;
		for (int i = 1; i <= offset; i++) {
			String xpathString = "/html/body/div[@id='wrapper']/div[@id='content']/div[@class='grid-16-8 clearfix']/div[@class='article']/div[2]/table["+i+"]/tbody/tr[@class='item']/td[2]/div[@class='pl2']/a";
			String linkNote = new Xpath(contentString, xpathString).getResult();
			java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<=(href=\")).*?(?=\")");
			if(linkNote!=null){
				Matcher m = p.matcher(linkNote); 
				if(m.find()){
					String tmp[] = m.group().split("/");
					String urlString2 = doubanApi+tmp[tmp.length-1];
					resultList.add(urlString2);
					//System.out.println(urlString2);
				}
			}
		}		
		return resultList;
	}
	
	@Override
	public List<String> parser2(String contentString,String urlString) throws JSONException {
		List<String>  resultList = new ArrayList<String>();
		// TODO Auto-generated method stub
		JSONObject myObject = new JSONObject(contentString);
		JSONObject attrs = myObject.getJSONObject("attrs");
		String country="日本";
		if(!attrs.isNull("country"))
			country = attrs.getJSONArray("country").get(0).toString();
		//System.out.println(country);
		String[] titles = myObject.getString("alt_title").split("/");
		String title = titles[0].trim();
		if(title.equalsIgnoreCase(""))
			title = myObject.getString("title");
		JSONArray imgArr = new JSONArray();
		JSONObject imgObject = new JSONObject();
		imgObject.put("source_path",myObject.get("image"));
		imgObject.put("mode", "small");
		imgArr.put(imgObject);
		String director="";
		if(!attrs.isNull("director"))
			 director = attrs.getJSONArray("director").get(0).toString().split(" ")[0];
		//System.out.println(director);
		JSONArray castArray = new JSONArray();
		if(!attrs.isNull("cast")){
			JSONArray tmpArray = attrs.getJSONArray("cast");	
			for (int i = 0; i < tmpArray.length(); i++) {
				castArray.put(tmpArray.get(i).toString().split(" ")[0]);
			}
		}
		int duration = 0;
		if(!attrs.isNull("movie_duration")){
			String tmpString = attrs.getString("movie_duration").split(",")[0];
			duration = getNumber(tmpString);
			//System.out.println(duration);
		}
		String publishtime = "0000-00-00";
		if(!attrs.isNull("year"))
			publishtime = getNumber(attrs.getString("year"))+"-01-01";
		String state = "完结";
		String mode = "movie";
		int play_num = 1;
		int all_play_num = 1;
		String description = myObject.getString("summary");
		JSONArray categoryArr = new JSONArray();
		if(!attrs.isNull("movie_type")){
			categoryArr = attrs.getJSONArray("movie_type");
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
    	
    	JSONArray ratingsArray = new JSONArray();
    	JSONObject myRatings = new JSONObject();
    	JSONObject tmpRatings = myObject.getJSONObject("rating");
    	myRatings.put("ratings_score", tmpRatings.get("average"));
    	myRatings.put("ratings_num", tmpRatings.get("numRaters"));
    	myRatings.put("source_site", "douban");
    	ratingsArray.put(myRatings);
    	
    	JSONArray authorArray = new JSONArray();
    	if(!myObject.isNull("author")){
    		JSONObject tmp = (JSONObject) myObject.getJSONArray("author").get(0);
    		authorArray.put(tmp.get("name"));
    	}
    	JSONArray sourArray = new JSONArray();
    	sourArray.put(urlString);
    	
    	
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("source_url", sourArray);
		jsonObj.put("title",title);
		jsonObj.put("img", imgArr);
		jsonObj.put("director", director);
		jsonObj.put("cast", castArray);
		jsonObj.put("author", authorArray);
		jsonObj.put("category", categoryArr);
		jsonObj.put("duration", duration);
		jsonObj.put("description", description);
		jsonObj.put("play_num", play_num);
		jsonObj.put("all_play_num", all_play_num);
		jsonObj.put("state", state);
		jsonObj.put("publish_time", publishtime);
		jsonObj.put("country", country);
		jsonObj.put("ratings", ratingsArray);
		jsonObj.put("timestamp", nowTime);
		jsonObj.put("mode", mode);
		resultList.add(jsonObj.toString());
		System.out.println("[INFO] " + title + SpiderConfig.SPLIT_STRING+(++count));

		return resultList;
	}

	@Override
	public List<String> parser3(String contentString,String urlString) {
		return null;
	}

	@Override
	public List<String> parser4(String contentString,String urlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser5(String contentString,String urlString) {
		// TODO Auto-generated method stub
		return null;
	}


	public int getNumber(String source){
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\s*([0-9]{1,20})\\s*");
		if(source!=null){
			Matcher m = p.matcher(source); 
			if(m.find()){
				return Integer.valueOf(m.group(1));
			}
		}	
		return 0;
		
	}	
	
	

}
