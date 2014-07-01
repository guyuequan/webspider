package com.hq.website;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NameList;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;
import com.hq.spider.util.Xpath;

public class dmmhkComic implements ParserRule{
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
	public List<String> parser1(String contentString,String urlString) throws UnsupportedEncodingException {
		List<String> resultList = new ArrayList<String>();
		contentString = new String(contentString.getBytes("iso8859-1"),"gbk");

		Xpath xpath = new Xpath(contentString, "/html/body/div[@id='content']/div[@id='page']/ul/span[1]");
		String pageString = xpath.getFilterHtmlResult();
		int pageCount = getNumber(pageString, "共", "页");
		resultList.add(urlString);
		String tmpString = urlString.replaceAll(".html", "");
		for (int i=2; i <=pageCount; i++) {
			resultList.add(tmpString+"_"+i+".html");
		}		
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString,String urlString) {
//		System.out.println(contentString);
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
//				System.out.println(baseUrl+m.group());
				resultList.add(baseUrl+m.group());
			}else {
				break;
			}	
		}
//		System.out.println(resultList);
		return resultList;
	}

	@Override
	public List<String> parser3(String contentString,String urlString) throws JSONException, UnsupportedEncodingException {
		List<String> resultList = new ArrayList<String>();
		String baseUrl = "http://www.dmm.hk";
		contentString = new String(contentString.getBytes("iso8859-1"),"gbk");

		String titleXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][1]/div[@class='row_right']";
		String stateXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][2]/div[@class='row_right']";
		String categoryXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][5]/div[@class='row_right']";
		String authorXpath = "/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='movie_info']/div[@class='row1'][3]/div[@class='row_right']/a/u";
		String imgXpath ="/html/body/div[@id='content']/div[@class='fbox']/div[2]/div[@class='con_250']/img/@src";
		String urlString2 = "/html/body/div[@id='content']/div[@class='fbox']/div[@class='urllist']/ul/li[";
		
		String title1 = null;
		
		String titleXpath2 = "/html/body/div[@id='content']/div[@class='fbox']/div[@class='tit']/h4";		
		String titleString2 = new Xpath(contentString, titleXpath2).getResult();
		String tmp[] = titleString2.split("&gt;&gt;");
		String tmp1 = tmp[2].split("<")[0];
		String nameList[] = tmp1.split("/");
		title1 = nameList[0].trim();

		
		String descriptionXpath = "/html/body/div[@id='content']/div[@class='about']/div[@class='text']";
		
		//	"/html/body/div[@id='content'][1]/div[@class='fbox']/div[2]/div[@class='con_250']/img/@src"
		
		String title = new Xpath(contentString, titleXpath).getFilterHtmlResult().replace("\n", "").replace(" ","").replace("完结", "");
	
		if(title1!=null)
			title = title1;
		
		String state = new Xpath(contentString, stateXpath).getFilterHtmlResult().replace("\n", "").replace(" ","");
		String category = new Xpath(contentString, categoryXpath).getFilterHtmlResult().replace("\n", "").replace(" ","");
		String author = new Xpath(contentString, authorXpath).getFilterHtmlResult();
		String img = new Xpath(contentString, imgXpath).getResult();
		String detail=new Xpath(contentString, descriptionXpath).getFilterHtmlResult();
		String description = "null" ;
//		String castString = "null";
		if(detail!=null){
			int t1 = detail.indexOf("内容介绍");
			int t2 = detail.indexOf("人物介绍");
			detail = detail.replaceAll("\n", " ").replaceAll(",", ";");
			if(t1>0&&t2>0)
				description = detail.substring(t1+5, t2-1).trim();
			int t3 = detail.indexOf("CAST");
//			castString = detail.substring(t3+5);
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
		int pageCount = i-2;	
		

		//String tmpString = "http://www.dmm.hk/play/2575/play_"+i+".html#0-"+(-1);
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<=(href=\")).*?(?=\")");
		if(playUrl!=null){
			Matcher m = p.matcher(playUrl); 
			m.find();
			playUrl = baseUrl+m.group();
		}
		playUrl = playUrl.split("_")[0];
		
		//存储最新的播放地址
		playUrl +="_"+pageCount+".html";
		
		// change the data to JSON format
		JSONArray categoryJA = new JSONArray();
		categoryJA.put(category);
		JSONArray authorJA = new JSONArray();
		authorJA.put(author);
		JSONArray imgJA = new JSONArray();
		JSONObject imgJO1 = new JSONObject();
		imgJO1.put("source_path", img);
		imgJO1.put("mode", "small");
		imgJA.put(imgJO1);
		JSONArray playJA = new JSONArray();
		JSONObject playJO1 = new JSONObject();
		playJO1.put("play_url", playUrl);
		playJO1.put("play_count", pageCount);
		playJO1.put("play_source", "dmm");
		playJA.put(playJO1);
		
		JSONArray castJA = new JSONArray();
		JSONArray actorJA = new JSONArray();
		String castString = null;
		int s = contentString.indexOf("CAST");
		int t;
		if(s != -1){
			s = contentString.indexOf("<br />", s);
			t = contentString.indexOf("</div>", s);
			castString = contentString.substring(s, t);
			String[] casts = castString.split("<br />");
			for (int j=0; j<casts.length; j++)
				if(casts[j].trim().length()>0){
					//get cast and actor
					String tmp2[] = null;
					if(casts[j].trim().indexOf("：")>0)
						tmp2 = casts[j].trim().split("：");
					else {
						tmp2 = casts[j].trim().split(":");
					}
					if(tmp2!=null&&tmp2.length>1){
						String myCast = tmp2[1].trim();
						String myActor =tmp2[0].trim();
						castJA.put(myCast);
						actorJA.put(myActor);
					}
				}
		}
		
		String publish_time = null;
		s = contentString.indexOf("发行时间");
		if (-1 != s) {
			while(contentString.charAt(s) != '>') s++;
			s++;
			t = contentString.indexOf("<br", s);
			publish_time = contentString.substring(s, t).replaceAll("&nbsp;","").replaceAll("：", "").replaceAll(":", "").trim();
			publish_time = publish_time.split("年")[0];
			publish_time += "-01-01";
		}
		
		
		JSONArray alias = new JSONArray();
		/*
		JSONObject alias1 = new JSONObject();
		s = contentString.indexOf("英文名称");
		if (-1 != s) {
			while(contentString.charAt(s) != '>') s++;
			s++;
			t = contentString.indexOf("<br", s);
			alias1.put("name", contentString.substring(s, t).replaceAll("：", "").replaceAll(":", "").trim());
			alias.put(alias1);
		}
		JSONObject alias2 = new JSONObject();
		s = contentString.indexOf("别名");
		if (-1 != s) {
			while(contentString.charAt(s) != '>') s++;
			s++;
			t = contentString.indexOf("<br", s);
			alias2.put("name", contentString.substring(s, t).replaceAll("：", "").replaceAll(":", "").trim());
			alias.put(alias2);
		}*/
		
		if (title1!=null) {
			if(nameList.length>1){
				JSONObject aliaObject = new JSONObject();
				aliaObject.put("name",nameList[1].trim());
				alias.put(aliaObject);
			}
				
		}
		
		JSONArray source_urlJA = new JSONArray();
		source_urlJA.put(urlString);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String timestamp = df.format(new Date());
		
	    int play_count = 0;
	    s = contentString.indexOf("观看人气");
	    if (-1 != s) {
	    	s = contentString.indexOf("<font color=red>", s);
	    	if (-1 != s) {
	    		s = s + "<font color=red>".length();
	    		t = s;
	    		while ('0'<=contentString.charAt(t) && contentString.charAt(t)<='9') t++;
	    		play_count = Integer.valueOf(contentString.substring(s, t));
	    	}
	    }
	    
		JSONObject jo = new JSONObject();
		title = title.trim();
		jo.put("title", title);
		jo.put("state", state);
		if(categoryJA.length()>0)
			jo.put("category", categoryJA);
		if(authorJA.length()>0)
			jo.put("author", authorJA);
		jo.put("img", imgJA);
		jo.put("play", playJA);
		jo.put("description", description);
		if(castJA.length()>0)
			jo.put("cast", castJA);
		if(actorJA.length()>0)
			jo.put("actor", actorJA);
		jo.put("publish_time", publish_time);
		if(alias.length()>0)
			jo.put("alias", alias);
		jo.put("source_url", source_urlJA);
		jo.put("timestamp", timestamp);
		jo.put("play_count", play_count);

		System.out.println("[INFO] " + title + SpiderConfig.SPLIT_STRING+(++count));
//		System.out.println("play_count:"+play_count);
		resultList.add(jo.toString());
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
