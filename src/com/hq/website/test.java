package com.hq.website;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;


import org.json.JSONArray;
import org.json.JSONException;

import com.hq.spider.core.Spider;
import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;


/**
 * test for spider
 * @author huqian.hq
 *
 */
public class test {
	
	 
	 /**
	 * douban spider,enter url:锟斤�?"http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB"
	 */
	 public static void douban(){

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	    String today = df.format(new Date());
		 SpiderConfig.DEFAULT_AGENT = SpiderConfig.USER_AGENT_CHROME;
		List<String>  urlList = new ArrayList<String>();
		int offset = 20;
		String url = "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB?start=";
		//get pages
		int start = 31;
		int end = 47;//47 pages
		for (int i = start; i <= end; i++) {			
			urlList.add(url+i*offset);
		}		
		ParserRule doubanComicRule = new doubanComic();
		Spider mySpider = new Spider(urlList, "d:/spider/douban/"+today+"-1.txt",doubanComicRule,2);
		mySpider.process();
	 }
	 /**
	  * dmm spider, enter url list :http://dmm.hk/list/list_    锟斤�? 1-24 �?
	  */
	 public static void dmmhk(){ 
		 List<String> urlList = new ArrayList<String>();
		 String baseUrl = "http://dmm.hk/list/list_";
		 int end = 24;
		 for (int i = 1; i <=end; i++) {
			urlList.add(baseUrl+i+".html");
		}
		ParserRule dmmhkComicRule = new dmmhkComic();
		Spider mySpider = new Spider(urlList, "d:/dmmhkfinal1.csv",dmmhkComicRule,3);
		mySpider.process();
	 }
	 /**
	  * tieba spider
	  * @param varString
	  * @param path
	  */
	 public static void tieba(String varString,int pages,String path){
		 String baseUrl="";
		 switch (varString) {
		case "author":
			baseUrl =  "http://tieba.baidu.com/f/fdir?fd=%B6%AF%C2%FE&sd=%B6%AF%C2%FE%D7%F7%D5%DF";
			break;
		case "cast":
			baseUrl = "http://tieba.baidu.com/f/fdir?fd=%B6%AF%C2%FE&sd=%B6%AF%C2%FE%C9%F9%D3%C5";
			break;
		case "actor":
			baseUrl = "http://tieba.baidu.com/f/fdir?fd=%B6%AF%C2%FE&sd=%B6%AF%C2%FE%BD%C7%C9%AB";
			break;
		default:
			break;
		}
		 int  pageCount =pages;
		 
		 
		List<String> urlList = new ArrayList<>();
		baseUrl+="&pn=";//+page
		for (int i = 1; i <=pageCount; i++) {
			urlList.add(baseUrl+i);
			System.out.println(baseUrl+i);
		}
		 
		 ParserRule tiebaRule = new tiebaComic();
		 Spider mySpider = new Spider(urlList, path,tiebaRule,1);
		 mySpider.process();
	 }
	 
	 /**
	  * xunleikankan, url: http://movie.kankan.com/type,order,area/anime,update,12/
	  * cost almost 560s
	  */
	 private static void xunleikankanComic() {
		 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	     String today = df.format(new Date());
		 List<String> urlList = new ArrayList<String>();
		 String baseUrl = "http://movie.kankan.com/type,order,area/anime,update,12/page";
		 int end = 62; 
		 int start = 1;
		 for (int i=start; i<=end; i++) {
			 urlList.add(baseUrl+i+"/");
		 }
		 ParserRule xunleikankanComicRule = new XunleikankanComicRule();
		 Spider xunleikankanSpider = new Spider(urlList, "d:/spider/xunleikankan/"+today+".txt", xunleikankanComicRule, 2);
		 xunleikankanSpider.process();
	 }
	 
	/**
	 */
	private static void tengXunShiPinComic() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	     String today = df.format(new Date());
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://v.qq.com/cartlist/";
		int count = 60;
		for (int i=0; i<=count; i++) {
			urlList.add(baseUrl + (i%10) + "/3_-1_1_-1_-1_0_" + i + "_1_10.html");
		}
		ParserRule qqComicRule = new TengXunShiPinComicRule();
		Spider qqSpider = new Spider(urlList, "d:/spider/tengxunshipin/"+today+".txt", qqComicRule, 2);
		qqSpider.process();
	}
	
	/**
	 * 涓嶅崱鐨勫姩鐢�
	 * http://www.bukade.com/board/cartoon/jp/index.html
	 */
	private static void bukade() {
		List<String> urlList = new ArrayList<String>();
		urlList.add("http://www.bukade.com/board/cartoon/jp/index.html");
		String baseUrl = "http://www.bukade.com/board/cartoon/jp/list_";
		int totalPageNum = 314;
		for(int i=2; i<=totalPageNum; i++){
			urlList.add(baseUrl + i + ".html");
		}
		ParserRule bukadeComicRule = new BukadeComicRule();
		Spider bukadeSpider = new Spider(urlList, "d:/spider/bukade/bukade.txt", bukadeComicRule, 2);
		bukadeSpider.process();
	}

	/**
	 * http://www.tudou.com/list/ach4a-2b120c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2sort2cla-2hot-2.html
	 */
	private static void tudou() {
		String url = "http://www.tudou.com/list/ach4a-2b120c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2sort2cla-2hot-2.html";
		ParserRule bukadeComicRule = new BukadeComicRule();
		Spider bukadeSpider = new Spider(url, "d:/spider/tudou/tudou.txt", bukadeComicRule, 2);
		bukadeSpider.process();
	}
	
	/**
	 * http://www.youku.com/v_olist/c_100_g__a_%E6%97%A5%E6%9C%AC_sg__mt__lg__q__s_1_r__u_0_pt_0_av_0_ag_0_sg__pr__h__d_1_p_1.html
	 */
	private static void youku() {
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://www.youku.com/v_olist/c_100_g__a_%E6%97%A5%E6%9C%AC_sg__mt__lg__q__s_1_r__u_0_pt_0_av_0_ag_0_sg__pr__h__d_1_p_";
		int totalPageNum = 29;
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i + ".html");
		}
		ParserRule youkuParserRule = new YoukuComicRule();
		Spider youkuSpider = new Spider(urlList, "d:/spider/youku/youku.txt", youkuParserRule, 2);
		youkuSpider.process();
	}
	
	/**
	 * http://so.tv.sohu.com/list_p1115_p20_p3_u65e5_u672c_p40_p50_p6-1_p74_p8_p9.html
	 */
	private static void sohuTV() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String today = df.format(new Date());
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://so.tv.sohu.com/list_p1115_p20_p3_u65e5_u672c_p40_p50_p6-1_p73_p8_p9_d20_p10";
		int totalPageNum = 10;
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i + "_p110.html");
		}
		ParserRule sohuTVParserRule = new SohuTVComicRule();
		Spider sohuTVSpider = new Spider(urlList, "d:/spider/sohuTV/"+today+".txt", sohuTVParserRule, 2);
		sohuTVSpider.process();
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		//sohuTV();
		//youku();// download绫讳腑涓嶉渶瑕佽浆鐮�?
//		tudou(); //鏈畬鎴�?
		//bukade();
		//tengXunShiPinComic();// download绫讳腑闇�瑕佽浆鐮�
		//xunleikankanComic();// download绫讳腑闇�瑕佽浆鐮�
		douban();
		//dmmhk();
		//tieba("cast",3,"d:/cast.csv");
		//tieba("author",3,"d:/author.csv");
	}
}
