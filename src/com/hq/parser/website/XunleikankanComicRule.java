package com.hq.parser.website;

import java.util.ArrayList;
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

	@Override
	public List<String> parser1(String contentString, String inputString) {
		List<String>  resultList = new ArrayList<String>();
		int itemsNum = 30;
		if(inputString.endsWith("page62/")) itemsNum = 17;
		for (int i=1; i<=itemsNum; i++) {
			String imgURLXpath = "//body//ul/li[" + i + "]/a/img/@_src";
			String detailXpath = "//body//ul/li[" + i + "]/a/@href";
			String titleXpath = "//body//ul/li[" + i + "]/a/img/@alt";
			
			String imgURL = new Xpath(contentString, imgURLXpath).getResult();
			String detailUrl = new Xpath(contentString, detailXpath).getResult();
			String title = new Xpath(contentString,titleXpath).getResult();
			
			String result = detailUrl + SpiderConfig.SPLIT_STRING + 
							title + SpiderConfig.SPLIT_STRING + 
							imgURL;
			resultList.add(result);			
		}
		
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) {
		List<String> resultList = new ArrayList<String>();
		String authorXpath = "//body//ul[@id='movie_info_ul']/li[@class='short_li'][2]/a";
		String voiceActorsXpath = "//body//ul[@id='movie_info_ul']/li[3]";
		String typesXpath = "//body//ul[@id='movie_info_ul']/li[5]";
		String descriptionXpath = "//body//p[@id='movie_info_intro_l']";
		
		//get author
		String author = new Xpath(contentString, authorXpath).getFilterHtmlResult();
		
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
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("play", strings[0]);
			jsonObj.put("title", strings[1]);
			jsonObj.put("img", strings[2]);
			jsonObj.put("author", author);
			jsonObj.put("cast", castJSONArray);
			jsonObj.put("category", categoryJSONArray);
			jsonObj.put("description", description);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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
