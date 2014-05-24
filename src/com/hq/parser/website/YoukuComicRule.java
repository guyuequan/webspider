package com.hq.parser.website;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class YoukuComicRule implements ParserRule {

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
	public List<String> parser2(String contentString, String inputString) {
		List<String> resultList = new ArrayList<String>();
		String titleXpath = "//body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@id='title_wrap']/div[@id='title']/div[@class='base']/h1[@class='title']/span[@class='name']";
		String modeXpath = "//body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@id='title_wrap']/div[@id='title']/div[@class='base']/h1[@class='title']/span[@class='edition']";
		String ratings_scoreXpath = "/html/body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@class='left']/div[@id='showInfo_wrap']/div[@id='showInfo']/div[@class='showInfo poster_w']/ul[@class='baseinfo']/li[@class='row1 rate']/span[@class='rating_dp']/em[@class='num']";
		String aliasXpath = "/html/body/div[@class='window']/div[@class='screen']/div[@class='s_body']/div[@class='s_main col2_21']/div[@class='left']/div[@id='showInfo_wrap']/div[@id='showInfo']/div[@class='showInfo poster_w']/ul[@class='baseinfo']/li[@class='row1 alias']";
		String publish_timeXpath = "//body//span[@class='pub'][1]";
		String categoryXpath = "//body//span[@class='type'][2]";
		String imgUrlXpath = "//body//li[@class='thumb']/img/@src";
		
		//名字
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult();
		//版本：TV/剧场版等
		String mode = new Xpath(contentString, modeXpath).getFilterHtmlResult();
		//评分
		String ratings_score = new Xpath(contentString, ratings_scoreXpath).getFilterHtmlResult();
		//别名
		String alias = new Xpath(contentString, aliasXpath).getFilterHtmlResult();
		if (alias != null) alias = alias.substring(5).trim();
		//发布时间
		String publish_time = new Xpath(contentString, publish_timeXpath).getFilterHtmlResult();
		if(publish_time != null){
			int i=0;
			for(; i<publish_time.length(); i++ )
				if ('0'<publish_time.charAt(i) && publish_time.charAt(i)<'9')
					break;
			publish_time = publish_time.substring(i, publish_time.length());
		}
		//类型
		JSONArray category = new JSONArray();
		String[] categoryStrings = new Xpath(contentString, categoryXpath).getFilterHtmlResult().split(" ");
		for(int j=0; j<categoryStrings.length; j++)
			if((!categoryStrings[j].equals("")) && (!categoryStrings[j].equals("类型:")))
				category.put(categoryStrings[j]);
		//声优
		JSONArray cast = new JSONArray();
		int start = contentString.indexOf("<label>声优:</label>");
		start = start + new String("<label>声优:</label>").length();
		int end = contentString.indexOf("</span>", start);
		String[] castStrings = contentString.substring(start, end).split("</a>");
		for(String singleCast : castStrings){
			int castNameStart = singleCast.length()-1;
			while(castNameStart>=0 && singleCast.charAt(castNameStart) != '>'){
				castNameStart -- ;
			}
			if(castNameStart > 0){
				cast.put(singleCast.substring(castNameStart+1));
			}
		}
		//集数、总集数
		String play_count = null, play_num = null;
		//状态：完结、连载
		String state = null;
		start = 0; end = 0;
		start = contentString.indexOf("<div class=\"basenotice\">");
		start = start + new String("<div class=\"basenotice\">").length();
		end = contentString.indexOf("<span", start);
		String playStateString = contentString.substring(start, end);
		String playState = "";
		for(char c : playStateString.toCharArray()){
			if('0'<=c && c<='9')
				playState = playState + c;
		}
		if(playStateString.contains("共")){
			state = "完结";
			play_num = playState;
		}
		else{
			state = "连载";
			play_count = playState;
		}
		//播放链接
		JSONArray play_url = new JSONArray();
		play_url.put(inputString);
		
		//播放来源
		JSONArray play_source = new JSONArray();
		play_source.put("优酷");
		
		//图片
		JSONArray img_url = new JSONArray();
		String imgUrl = new Xpath(contentString, imgUrlXpath).getResult();
		img_url.put(imgUrl);
		
		//最终结果数据
		JSONObject jsonObj = new JSONObject(); 
		try {
			jsonObj.put("play_url", play_url);
			jsonObj.put("play_source", play_source);
			jsonObj.put("title", title);
			jsonObj.put("mode", mode);
			jsonObj.put("ratings_score", ratings_score);
			jsonObj.put("alias", alias);
			jsonObj.put("publish_time", publish_time);
			jsonObj.put("category", category);
			jsonObj.put("cast", cast);
			jsonObj.put("state", state);
			if (play_num != null) jsonObj.put("play_num", play_num);
			if (play_count != null) jsonObj.put("play_count", play_count);
			jsonObj.put("img_url", img_url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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
