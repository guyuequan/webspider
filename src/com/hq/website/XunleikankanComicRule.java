package com.hq.website;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

/**
 * The parser for XunLeiKanKan (http://movie.kankan.com/type,order,area/anime,update,12/)
 * @author andone.lsl
 *
 */
public class XunleikankanComicRule implements ParserRule{

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
	public List<String> parser1(String contentString, String inputString) throws UnsupportedEncodingException {
		List<String>  resultList = new ArrayList<String>();
		int itemsNum = 30;
		for (int i=1; i<=itemsNum; i++) {
			String imgURLXpath = "//body//ul/li[" + i + "]/a/img/@_src";
			String detailXpath = "//body//ul/li[" + i + "]/a/@href";
			String titleXpath = "//body//ul/li[" + i + "]/a/img/@alt";
			
			String imgURL = new Xpath(contentString, imgURLXpath).getResult();
			String detailUrl = new Xpath(contentString, detailXpath).getResult();
			String title = new Xpath(contentString,titleXpath).getResult();
			if(title!=null){
				String tmpString = title;
				title = new String(tmpString.getBytes("iso8859-1"),"utf-8");
				String result = detailUrl + SpiderConfig.SPLIT_STRING + 
								title + SpiderConfig.SPLIT_STRING + 
								imgURL;
			//	System.out.println("[TITLE]"+title+inputString.split(SpiderConfig.SPLIT_STRING)[0]);
				resultList.add(result);			
			}else{
				//System.out.println("[NULL]"+inputString.split(SpiderConfig.SPLIT_STRING)[0]);
			}
		}
		
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) throws JSONException, UnsupportedEncodingException {
		contentString = new String(contentString.getBytes("iso8859-1"),"gbk");
		List<String> resultList = new ArrayList<String>();
		String directorXpath = "//body//ul[@id='movie_info_ul']/li[@class='short_li'][2]/a";
		String voiceActorsXpath = "//body//ul[@id='movie_info_ul']/li[3]";
		String typesXpath = "//body//ul[@id='movie_info_ul']/li[5]";
		String descriptionXpath = "//body//p[@id='movie_info_intro_l']";
		String playNumXpath="//body//ul[@id='movie_info_ul']/li[@class='short_li'][1]/span";
		String countryXpath = "//body//ul[@id='movie_info_ul']/li[4]";
		String publishTimeXpath = "//body//div[@class='movieinfo_tt']/h2/span";
		String playCountXpath = "//body//div[@class='movieinfo_tt']/span[@class='movieinfo_num']";
		
		//get country
		String countryTmp = new Xpath(contentString,countryXpath).getFilterHtmlResult();
		String country="日本";
		if(countryTmp!=null){
			if(countryTmp.contains("地区:")){
				String tmp[] = countryTmp.trim().split(":");
				if(tmp.length>1)
					country = tmp[1];
				//System.out.println(country);
			}
		}
		
		//get play count
		String playCountString = new Xpath(contentString, playCountXpath).getFilterHtmlResult().trim().replaceAll(",", "");
		int playCount = getNumber(playCountString, "播放:", "");
		//get play num
		int update_play_num = 0;//latest play num
		int all_play_num = 0;//all play num
		String play_num_string = new Xpath(contentString,playNumXpath).getFilterHtmlResult().trim();
		update_play_num = getNumber(play_num_string, "更新至", "集");
		all_play_num = getNumber(play_num_string, "共", "集");
		if(all_play_num==0)
			all_play_num = getNumber(play_num_string, "", "集全");
		String stateString = "连载";
		if(all_play_num!=0&&update_play_num==0){
			stateString = "完结";
			update_play_num = all_play_num;
		}
		//get publish_time
		String publish_time = new Xpath(contentString, publishTimeXpath).getFilterHtmlResult().trim();
		if(publish_time.length()==4){
			//only year
			publish_time += "-01-01";
		}
		//get author
	//	JSONArray directorArray = new JSONArray();
		String director = new Xpath(contentString, directorXpath).getFilterHtmlResult();
		//directorArray.put(director);
		
		//get voiceActors
		String voiceActorsElements = new Xpath(contentString, voiceActorsXpath).getResult();
		JSONArray castJSONArray = new JSONArray();
		Matcher m1 = Pattern.compile("</a>").matcher(voiceActorsElements);
		int nums1 = 0;
		while(m1.find()){
			nums1 ++;
			String singleVoiceActorXpath = "//a["+nums1+"]";
			castJSONArray.put(new Xpath(voiceActorsElements, singleVoiceActorXpath).getFilterHtmlResult());
        }
		
		//get types
		String typesElements = new Xpath(contentString, typesXpath).getResult();
		JSONArray categoryJSONArray = new JSONArray();
		Matcher m2 = Pattern.compile("</a>").matcher(typesElements);
		int nums2 = 0;
		while(m2.find()){
			nums2 ++;
			String singleTypeXpath = "//a[" + nums2 + "]";
			categoryJSONArray.put(new Xpath(typesElements, singleTypeXpath).getFilterHtmlResult());
		}
		
		//get description
		String description = new Xpath(contentString, descriptionXpath).getFilterHtmlResult();
		description = description.replace("[查看详情]", "");
		
		String[] strings = inputString.split(SpiderConfig.SPLIT_STRING);
		JSONArray playJA = new JSONArray();
		JSONObject jObject = new JSONObject();
		jObject.put("play_url", strings[0]);
		jObject.put("play_count",update_play_num);
		jObject.put("play_source","xunleikankan");
		playJA.put(jObject);
		JSONArray imgJA = new JSONArray();
		JSONObject iObject = new JSONObject();
		iObject.put("source_path", strings[2]);
		iObject.put("mode", "small");
		imgJA.put(iObject);
		
		JSONArray play_source = new JSONArray();
		play_source.put(inputString.split(SpiderConfig.SPLIT_STRING)[0]);
		
		JSONObject jsonObj = new JSONObject();
		
		//get time
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
    	String title = strings[1].trim();
    	
		try {
			jsonObj.put("play", playJA);
			jsonObj.put("source_url", play_source);
			jsonObj.put("title", strings[1]);
			jsonObj.put("img", imgJA);
			jsonObj.put("director", director);
			jsonObj.put("cast", castJSONArray);
			jsonObj.put("category", categoryJSONArray);
			jsonObj.put("description", description);
			jsonObj.put("play_num", update_play_num);
			jsonObj.put("all_play_num", all_play_num);
			jsonObj.put("state", stateString);
			jsonObj.put("publish_time", publish_time);
			jsonObj.put("play_count", playCount);
			jsonObj.put("country", country);
			jsonObj.put("timestamp", nowTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("[INFO] " + strings[1] + SpiderConfig.SPLIT_STRING+(++count));
		resultList.add(jsonObj.toString());
		return resultList;
	}

	@Override
	public List<String> parser3(String contentString, String urlString) {
		return null;
	}

	@Override
	public List<String> parser4(String contentString, String urlString) {
		return null;
	}

	@Override
	public List<String> parser5(String contentString, String urlString) {
		return null;
	}

}
