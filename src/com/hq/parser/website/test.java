package com.hq.parser.website;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;


import com.hq.spider.Spider;
import com.hq.spider.parser.ParserRule;


/**
 * test for spider
 * @author huqian.hq
 *
 */
public class test {
	
	 
	 /**
	 * douban spider,enter url:��"http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB"
	 */
	 public static void douban(){
		String enterUrl = "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB";
		ParserRule doubanComicRule = new doubanComic();
		Spider mySpider = new Spider(enterUrl, "d:/douban.txt",doubanComicRule,3);
		mySpider.process();
	 }
	 /**
	  * dmm spider, enter url list :http://dmm.hk/list/list_    �� 1-24 ҳ
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
	  * xunleikankan spider, url: http://movie.kankan.com/type,order,area/anime,update,12/
	  */
	 private static void xunleikankanComic() {
		 List<String> urlList = new ArrayList<String>();
		 String baseUrl = "http://movie.kankan.com/type,order,area/anime,update,12/page";
		 int end = 62;
		 for (int i=1; i<=end; i++) {
			 urlList.add(baseUrl+i+"/");
		 }
		 ParserRule xunleikankanComicRule = new XunleikankanComicRule();
		 Spider xunleikankanSpider = new Spider(urlList, "d:/spider/xunleikankan/xunleikankan.txt", xunleikankanComicRule, 2);
		 xunleikankanSpider.process();
	 }
	 
	private static void tengXunShiPinComic() {
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://v.qq.com/cartlist/";
		for (int i=0; i<=60; i++) {
			urlList.add(baseUrl + (i%10) + "/3_-1_1_-1_-1_0_" + i + "_1_10.html");
		}
		ParserRule tengXunShiPinComicRule = new TengXunShiPinComicRule();
		Spider xunleikankanSpider = new Spider(urlList, "d:/spider/tengxunshipin/tengxunshipin.txt", tengXunShiPinComicRule, 2);
		xunleikankanSpider.process();
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		tengXunShiPinComic();
		//xunleikankanComic();
		//douban();
		//dmmhk();
		//tieba("cast",3,"d:/cast.csv");
		//tieba("author",3,"d:/author.csv");
	}
}