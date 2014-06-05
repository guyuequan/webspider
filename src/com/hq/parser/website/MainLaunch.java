cpackage com.hq.parser.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hq.spider.Spider;
import com.hq.spider.parser.ParserRule;

public class MainLaunch {
	
	/**
	 * comic spider for xunleikankan 
	 */
	 private static void xunleikankanComic() {
		 System.out.println("[INFO] spider start for xunleikankan");
		 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	     String today = df.format(new Date());
		 List<String> urlList = new ArrayList<String>();
		 String baseUrl = "http://movie.kankan.com/type,order,area/anime,update,12/page";
		 int end = 1;
		 for (int i=1; i<=end; i++) {
			 urlList.add(baseUrl+i+"/");
		 }
		 ParserRule xunleikankanComicRule = new XunleikankanComicRule();
		 Spider xunleikankanSpider = new Spider(urlList, "d:/spider/xunleikankan/"+today+".txt", xunleikankanComicRule, 2);
		 xunleikankanSpider.process();
	 }
	 
	
	public static void main(String[] args) {
		xunleikankanComic();
	}
}
