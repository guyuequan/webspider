package com.hq.parser.website;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import com.hq.spider.parser.Parserrule;
import com.hq.spider.util.Xpath;

public class dmmhkComic implements Parserrule{

	@Override
	public List<String> parser1(String contentString,String urlString) {
		List<String> resultList = new ArrayList<String>();
	//	System.out.println(contentString);
		Xpath xpath = new Xpath(contentString, "/html/body/div[@id='content']/div[@id='page']/ul/span[1]");
		String pageString = xpath.getFilterHtmlResult();
		
		//int s = pageString.indexOf("ҳ");
		int t = pageString.indexOf("页");
		Integer pageCount =Integer.valueOf(pageString.substring(1, t));
		resultList.add(urlString);
		String tmpString = urlString.replaceAll(".html", "");
		
		//pageCount = 3;
		for (int i = 1; i <=pageCount; i++) {
			resultList.add(tmpString+"_"+i+".html");
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString,String urlString) {
		// TODO Auto-generated method stub
		
	//	System.out.println(contentString);
		List<String> resultList = new ArrayList<>();
		String baseUrl = "http://www.dmm.hk";
		int offset = 16;
		
		for (int i = 1; i <=offset; i++) {
			String xpathString ="/html/body/div[@id='content']/div[@class='flist']/div[@id='borderlist']/div[@id='kuailist']["+i+"]/div[@class='kr']/div[@class='downcat']/a";
			Xpath xpath = new Xpath(contentString, xpathString);
			String resString = xpath.getResult();
			
			java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<=(href=\")).*?(?=\")");
			if(resString!=null){
				Matcher m = p.matcher(resString); 
				m.find();
				System.out.println(baseUrl+m.group());
				resultList.add(baseUrl+m.group());
			}else {
				break;
			}	
		}
		return resultList;
	}

	@Override
	public List<String> parser3(String contentString,String urlString) {
		// TODO Auto-generated method stub
		
		
		List<String> resultList = new ArrayList<String>();
		String baseUrl = "http://www.dmm.hk";
		
		String titleXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][1]/div[@class='row_right']";
		String stateXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][2]/div[@class='row_right']";
		String categoryXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][5]/div[@class='row_right']";
		String authorXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][3]/div[@class='row_right']/a/u";
		String imgXpath ="/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='con_250']/img/@src";
		String urlString2 = "/html/body/div[@id='content']/div[@class='fbox']/div[@class='urllist']/ul/li[";
		
		String descriptionXpath = "/html/body/div[@id='content']/div[@class='about']/div[@class='text']";
		
		//	"/html/body/div[@id='content'][1]/div[@class='fbox']/div[2]/div[@class='con_250']/img/@src"
		
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult().replace("\n", "").replace(" ","").replace("完结", "");
		String state = new Xpath(contentString, stateXpath).getFilterHtmlResult().replace("\n", "").replace(" ","");
		String category = new Xpath(contentString, categoryXpath).getFilterHtmlResult().replace("\n", "").replace(" ","");
		String author = new Xpath(contentString, authorXpath).getFilterHtmlResult();
		String img = new Xpath(contentString, imgXpath).getResult();
		String detail=new Xpath(contentString, descriptionXpath).getFilterHtmlResult();
		String description = "null" ;
		String castString = "null";
		if(detail!=null){
			int t1 = detail.indexOf("内容介绍");
			int t2 = detail.indexOf("人物介绍");
			detail = detail.replaceAll("\n", " ").replaceAll(",", ";");
			if(t1>0&&t2>0)
				description = detail.substring(t1+5, t2-1);
				int t3 = detail.indexOf("CAST");
				castString = detail.substring(t3+5);
		}
		String urlString3 = urlString2+"1]/a";
		String playUrl = new Xpath(contentString, urlString3).getResult();
		
		//��̽url
		String tmpPlayUrl=playUrl;
		int i = 1;
		while(tmpPlayUrl!=null){
			String tmpsString = urlString2+i+"]/a";
			i++;
			tmpPlayUrl = new Xpath(contentString, tmpsString).getResult();
		}
		int pageCount = i-1;	
		

		//String tmpString = "http://www.dmm.hk/play/2575/play_"+i+".html#0-"+(-1);
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<=(href=\")).*?(?=\")");
		if(playUrl!=null){
			Matcher m = p.matcher(playUrl); 
			m.find();
			playUrl = baseUrl+m.group();
		}
		playUrl = playUrl.split("_")[0];		
		String result = title+","+state+","+category+","+author+","+img+","+playUrl+",$.html#0-($-1),"+pageCount+","+description+","+castString;	
		System.out.println(result);
		System.out.println(urlString);
		//System.out.println(img);
		resultList.add(result);
		return resultList;
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

}
