package com.hq.parser.website;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hq.spider.parser.ParserRule;

public class WeiboParser implements ParserRule{

	@Override
	public List<String> parser1(String contentString, String inputString) {
		// TODO Auto-generated method stub
		System.out.println(inputString);
		System.out.println("==========================");
		System.out.println(contentString);
		
		List<String>  resultList = new ArrayList<String>();
	      Document htmlDoc = Jsoup.parse(contentString);
	      Elements elements = htmlDoc.getElementsByTag("script");
	      Element first = elements.first();
	      String content = first.html();
	      int firstIndex = content.indexOf("fid");
	      int endIndex = content.indexOf("ad_afr");
	      String containerId = content.substring(firstIndex+5, endIndex-2);

	      String baseUrl = "http://m.weibo.cn/page/json?containerid="+containerId
	      +"_-_WEIBO_SECOND_PROFILE_WEIBO&rl=2&luicode=10000011&lfid="+containerId+"&uicode=10000012&fid=" +
	      containerId+"_-_WEIBO_SECOND_PROFILE_WEIBO&ext=sourceType%3A&page=";
	         
	      for (int i = 1; i <= 20; i++) {         
	    	  System.out.println(baseUrl+i);
	    	  resultList.add(baseUrl+i);
	      }
	      return resultList;
		
		/*List<String>  resultList = new ArrayList<String>();
		System.out.println(contentString);
		System.out.println("**********************");
		contentString = contentString.replaceAll("\\\\","");
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<=(href=\")).*?(?=\")");
		if(contentString!=null){
			Matcher m = p.matcher(contentString); 
			while(m.find()){
				String tmp = m.group();
				if(!tmp.contains("javascript")){
					System.out.println("------"+tmp);
					resultList.add(tmp);
				}
			}
		}
		return resultList;*/
	}

	@Override
	public List<String> parser2(String contentString, String inputString) {
		List<String>  resultList = new ArrayList<String>();
	      Document htmlDoc = Jsoup.parse(contentString);
	      Elements elements = htmlDoc.getElementsByTag("script");
	      Element first = elements.first();
	      String content = first.html();
	      int firstIndex = content.indexOf("fid");
	      int endIndex = content.indexOf("ad_afr");
	      String containerId = content.substring(firstIndex+5, endIndex-2);

	      String baseUrl = "http://m.weibo.cn/page/json?containerid="+containerId
	      +"_-_WEIBO_SECOND_PROFILE_WEIBO&rl=2&luicode=10000011&lfid="+containerId+"&uicode=10000012&fid=" +
	      containerId+"_-_WEIBO_SECOND_PROFILE_WEIBO&ext=sourceType%3A&page=";
	         
	      for (int i = 1; i <= 20; i++) {         
	    	  System.out.println(baseUrl+i);
	    	  resultList.add(baseUrl+i);
	      }
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
