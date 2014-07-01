package com.hq.website;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

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
    static String today = null;
    
    public static void init(){
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        today = df.format(new Date());
        
    }
    
    /**
     * douban,enter url :http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB?start=
     */
	 public static void douban(){

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	    String today = df.format(new Date());
		 SpiderConfig.DEFAULT_AGENT = SpiderConfig.USER_AGENT_CHROME;
		List<String>  urlList = new ArrayList<String>();
		int offset = 20;
		String url = "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB?start=";
		//get pages
		int start = 1;
		int end = 47;//47 pages
		for (int i = start; i <= end; i++) {			
			urlList.add(url+i*offset);
		}		
		ParserRule doubanComicRule = new doubanComic();
		Spider mySpider = new Spider(urlList, "d:/spider/douban/"+today+".txt",doubanComicRule,2);
		mySpider.process();
	 }
	 
	 /**
	  * dmm spider, enter url list: http://dmm.hk/list/list_1.html
	  */
	 public static void dmmhk() {
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://dmm.hk/list/list_";
		int end = 24;
		for (int i = 1; i <=end; i++) {
			urlList.add(baseUrl+i+".html");
		}
		ParserRule dmmhkComicRule = new dmmhkComic();
		Spider mySpider = new Spider(urlList, "d:/spider/dmm/"+today+".txt",dmmhkComicRule,3);
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
		 String baseUrl = "http://movie.kankan.com/type,order/anime,update/page";
		 int end = 75; 
		 int start = 1;
		 for (int i=start; i<=end; i++) {
			 urlList.add(baseUrl+i+"/");
		 }
		 System.out.println("[INFO] enter url size:"+urlList.size());
		 ParserRule xunleikankanComicRule = new XunleikankanComicRule();
		 Spider xunleikankanSpider = new Spider(urlList, "d:/spider/xunleikankan/"+today+".txt", xunleikankanComicRule, 2);
		 xunleikankanSpider.process();
	 }
	 
	/**
	 * qq
	 */
	private static void tengXunShiPinComic() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	    String today = df.format(new Date());
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://v.qq.com/cartlist/";
		int count = 60;
		for (int i=0; i<=count; i++) {
			urlList.add(baseUrl + (i%10) + "/3_-1_-1_-1_-1_0_" + i + "_1_10.html");
		}
		ParserRule qqComicRule = new TengXunShiPinComicRule();
		Spider qqSpider = new Spider(urlList, "d:/spider/tengxunshipin/"+today+".txt", qqComicRule, 2);
		qqSpider.process();
	}
	
	/**
	 * 不卡的动漫
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
	 * http://www.youku.com/v_olist/c_100_g__a_%E6%97%A5%E6%9C%AC_sg__mt__lg__q__s_1_r__u_0_pt_0_av_0_ag_0_sg__pr__h__d_1_p_1.html
	 * @throws IOException 
	 */
	private static void youku() throws IOException {
	/*	Path imagesPath = Paths.get("d:/spider/youku/"+today+"/images/");
		if (Files.notExists(imagesPath)) {
			Files.createDirectories(imagesPath);
		}*/
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://www.youku.com/v_olist/c_100_g__a_%E6%97%A5%E6%9C%AC_sg__mt__lg__q__s_1_r__u_0_pt_0_av_0_ag_0_sg__pr__h__d_1_p_";
		int totalPageNum = 29;
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i + ".html");
		}
		ParserRule youkuParserRule = new YoukuComicRule();
		Spider youkuSpider = new Spider(urlList, "d:/spider/youku/"+today+".txt", youkuParserRule, 2);
		youkuSpider.process();
	}
	
	/**
	 * http://so.tv.sohu.com/list_p1115_p20_p3_p40_p50_p6-1_p74_p8_p9_d20_p101_p110.html:1
	 * @throws IOException 
	 */
	private static void sohuTV() throws IOException {
	/*	Path imagesPath = Paths.get("d:/spider/sohuTV/"+today+"/images/");
		if (Files.notExists(imagesPath)) {
			Files.createDirectories(imagesPath);
		}*/
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://so.tv.sohu.com/list_p1115_p20_p3_p40_p50_p6-1_p74_p8_p9_d20_p10";
		int totalPageNum = 29;
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i + "_p110.html");
		}
		ParserRule sohuTVParserRule = new SohuTVComicRule();
		Spider sohuTVSpider = new Spider(urlList, "d:/spider/sohuTV/"+today+".txt", sohuTVParserRule, 2);
		sohuTVSpider.process();
	}
	
	
	/**
	 * http://list.iqiyi.com/www/4/------------2-1-1-1---.html
	 * @throws IOException 
	 */
	private static void iqiyi() throws IOException {
	/*	Path imagesPath = Paths.get("d:/spider/iqiyi/"+today+"/images/");
		if (Files.notExists(imagesPath)) {
			Files.createDirectories(imagesPath);
		}*/
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://list.iqiyi.com/www/4/------------2-1-";
		int totalPageNum = 90;
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i + "-1---.html");
		}
		ParserRule iqiyiComicRule =  new IqiyiComicRule();
		Spider iqiyiSpider = new Spider(urlList, "d:/spider/iqiyi/"+today+".txt", iqiyiComicRule, 2);
		iqiyiSpider.process();
	}

	/**
	 * 
	 * @param args
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static void letv(){		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String today = df.format(new Date());
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://list.letv.com/apin/chandata.json?c=5&d=1&md=&o=9&s=1&p=";
		int totalPageNum = 91;
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i );
		}
		ParserRule letvComicRule = new LetvComicRule();
		Spider mySpider = new Spider(urlList, "d:/spider/letv/"+today+".txt", letvComicRule, 1);
		mySpider.process();
	}
	
	/**
	 * pptv download:http://list.pptv.com/api/3---------91.json?action=getListChannel&filter=channel_list,config&cb=waterfall.append
	 */
	private static void pptv() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String today = df.format(new Date());
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://list.pptv.com/api/3---------";
		String baseUrl2 = ".json?action=getListChannel&filter=channel_list,m_sort";
		int totalPageNum =91;
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i+baseUrl2 );
		}
		ParserRule pptvComicRule = new PptvComicRule();
		Spider mySpider = new Spider(urlList, "d:/spider/pptv/"+today+".txt", pptvComicRule, 1);
		mySpider.process();
	}
	
	/**
	 * tudou download:http://www.tudou.com/list/getPageData.action?page=22&sort=2&tagType=3&firstTagId=4
	 */
	private static void tudou() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String today = df.format(new Date());
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://www.tudou.com/list/getPageData.action?page=";
		String baseUrl2 = "&sort=2&tagType=3&firstTagId=4&tags=";
		//由于动漫资源需要按照国家来区分
		Map<Integer,String> tagCountry = new HashMap<Integer, String>();
		//日本 大陆 美国 韩国 英国 法国 台湾 香港	
		tagCountry.put(120, "日本");
		tagCountry.put(121, "中国大陆");
		tagCountry.put(122, "美国");
		tagCountry.put(123, "韩国");
		tagCountry.put(124, "英国");
		tagCountry.put(125, "法国");
		tagCountry.put(126, "台湾");
		tagCountry.put(127, "香港");
		Map<Integer,Integer> keyCount = new HashMap<Integer, Integer>();
		//日本 大陆 美国 韩国 英国 法国 台湾 香港	
		keyCount.put(120,24);
		keyCount.put(121,24);
		keyCount.put(122,1);
		keyCount.put(123,1);
		keyCount.put(124,1);
		keyCount.put(125,1);
		keyCount.put(126,1);
		keyCount.put(127,1);
		String keyString1 = "AlbumParam_firstTagId%3D4_c1%3D-2_c2%3D";
		String keyString2 = "_c3%3D-2_c4%3D-2_c5%3D-2_c6%3D-2_c7%3D-2_c8%3D-2_c9%3D-2_c10%3D-2"+
				"_c11%3D-2_c12%3D-2_c13%3D-2_c14%3D-2_tagType%3D3_hot%3D-2_so%3D2";
		for (int j = 120; j <= 127; j++) {
			int totalPageNum =keyCount.get(j);
			for(int i=0; i<=totalPageNum; i++){		
				urlList.add(baseUrl + i+baseUrl2+j+"&key="+keyString1+j+keyString2+SpiderConfig.SPLIT_STRING+tagCountry.get(j) );
			}
		}
		ParserRule tudouComicRule = new TudouComicRule();
		Spider mySpider = new Spider(urlList, "d:/spider/tudou/"+today+".txt", tudouComicRule, 1);
		mySpider.process();
	}
	
	/**
	 * page:112
	 * pptv download:http://v.baidu.com/commonapi/comic2level/?order=hot&pn=2
	 **/
	private static void baidu() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String today = df.format(new Date());
		List<String> urlList = new ArrayList<String>();
		String baseUrl = "http://v.baidu.com/commonapi/comic2level/?order=hot&pn=";		
		int totalPageNum =112;//pages: 112  every page: 18 items
		for(int i=1; i<=totalPageNum; i++){
			urlList.add(baseUrl + i);
		}
		ParserRule baiduComicRule = new BaiduComicRule();
		Spider mySpider = new Spider(urlList, "d:/spider/baidu/"+today+".txt", baiduComicRule, 1);
		mySpider.process();
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		init();
		//经过测试的网站有:
	/*	sohuTV();
		youku();
		tudou(); 
		xunleikankanComic();
		letv();
		pptv();
		iqiyi()//slow
		baidu();*/
		
		//bukade();
		//tengXunShiPinComic();
		//douban();//需要重构
		dmmhk();
		//tieba("cast",3,"d:/cast.csv");
		//tieba("author",3,"d:/author.csv");
	}

}
