package com.hq.parser.website;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

import com.hq.spider.Spider;
import com.hq.spider.parser.ParserRule;

/**
 * test for spider
 * 
 * @author huqian.hq
 * 
 */
public class test {

	/**
	 * douban spider,enter url:��
	 * "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB"
	 */
	public static void douban() {
		String enterUrl = "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB";
		ParserRule doubanComicRule = new doubanComic();
		Spider mySpider = new Spider(enterUrl, "d:/douban.txt",
				doubanComicRule, 3);
		mySpider.process();
	}

	/**
	 * dmm spider, enter url list :http://dmm.hk/list/list_ �� 1-24 ҳ
	 */
	public static void dmmhk() {
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://dmm.hk/list/list_";
		int end = 24;
		for (int i = 1; i <= end; i++) {
			urlList.add(baseUrl + i + ".html");
		}
		ParserRule dmmhkComicRule = new dmmhkComic();
		Spider mySpider = new Spider(urlList, "d:/dmmhkfinal1.csv",
				dmmhkComicRule, 3);
		mySpider.process();
	}

	/**
	 * tieba spider
	 * 
	 * @param varString
	 * @param path
	 */
	public static void tieba(String varString, int pages, String path) {
		String baseUrl = "";
		switch (varString) {
		case "author":
			baseUrl = "http://tieba.baidu.com/f/fdir?fd=%B6%AF%C2%FE&sd=%B6%AF%C2%FE%D7%F7%D5%DF";
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
		int pageCount = pages;

		List<String> urlList = new ArrayList<>();
		baseUrl += "&pn=";// +page
		for (int i = 1; i <= pageCount; i++) {
			urlList.add(baseUrl + i);
			System.out.println(baseUrl + i);
		}

		ParserRule tiebaRule = new tiebaComic();
		Spider mySpider = new Spider(urlList, path, tiebaRule, 1);
		mySpider.process();
	}

	/**
	 * xunleikankan spider, url:
	 * http://movie.kankan.com/type,order,area/anime,update,12/
	 */
	private static void xunleikankanComic() {
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://movie.kankan.com/type,order,area/anime,update,12/page";
		int end = 62;
		for (int i = 1; i <= end; i++) {
			urlList.add(baseUrl + i + "/");
		}
		ParserRule xunleikankanComicRule = new XunleikankanComicRule();
		Spider xunleikankanSpider = new Spider(urlList,
				"d:/spider/xunleikankan/xunleikankan.txt",
				xunleikankanComicRule, 2);
		xunleikankanSpider.process();
	}

	private static void tengXunShiPinComic() {
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://v.qq.com/cartlist/";
		for (int i = 0; i <= 60; i++) {
			urlList.add(baseUrl + (i % 10) + "/3_-1_1_-1_-1_0_" + i
					+ "_1_10.html");
		}
		ParserRule tengXunShiPinComicRule = new TengXunShiPinComicRule();
		Spider xunleikankanSpider = new Spider(urlList,
				"d:/spider/tengxunshipin/tengxunshipin.txt",
				tengXunShiPinComicRule, 2);
		xunleikankanSpider.process();
	}

	private static void weibo() {
		ArrayList<String> urlList = new ArrayList<String>();
		int k = 1;
		String baseUrl = "http://club.weibo.cn/aj/getrank?type=2&page=";
		for (int i = 1; i <= k; i++) {
			urlList.add(baseUrl + i);
		}

		ParserRule weiboRule = new WeiboParser();
		Spider weiboSpider = new Spider(urlList, "d:/spider/weibo7.txt",
				weiboRule, 3);
		weiboSpider.process();
	}

	public static void weiboRank() throws IOException{
		
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("d:/spider/hoturl.txt")));
		String line = reader.readLine();
		List<String> urlList = new ArrayList<String>();
		while(line!=null){
			urlList.add(line);
			line = reader.readLine();
		}
		//String urlString="http://data.weibo.com/top/keyword?t=all";
		WeiboRankParser weiboRankParser = new WeiboRankParser();
		Spider spider = new Spider(urlList, "d:/spider/rank.txt", weiboRankParser, 1);
		spider.process();
	}
	
	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		weiboRank();
//		filterLink();
		// tengXunShiPinComic();
		// xunleikankanComic();
		// douban();
		// dmmhk();
		// tieba("cast",3,"d:/cast.csv");
		// tieba("author",3,"d:/author.csv");
	}

	public static void filterLink() {
		String readPath = "D:/spider/weibo.txt";
		String writePath = "D:/spider/weibo_process.txt";
		File filename = new File(readPath);
		File writeFile = new File(writePath);
		InputStreamReader reader = null;
		BufferedWriter out = null;
		try {
			writeFile.createNewFile();
			out = new BufferedWriter(new FileWriter(writeFile));  
			reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();
			while (line != null) {
				System.out.println("******************************");
				line = br.readLine(); // 一次读入一行数据
				System.out.println("the read data is : " + line);
				String tmp = htmlRemoveTag(line);
				System.out.println("the handle data is : " + tmp);
				out.write(tmp + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除Html标签
	 * 
	 * @param inputString
	 * @return
	 */
	public static String htmlRemoveTag(String inputString) {
		if (inputString == null)
			return null;
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
//		Pattern p_script;
//		java.util.regex.Matcher m_script;
//		Pattern p_style;
//		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		try {
			//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
//			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
			//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
//			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
//			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
//			m_script = p_script.matcher(htmlStr);
//			htmlStr = m_script.replaceAll(""); // 过滤script标签
//			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
//			m_style = p_style.matcher(htmlStr);
//			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(inputString);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}
	
}
