package com.hq.website;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.hq.spider.util.Httphandle;
import com.hq.spider.util.Xpath;

public class test2 {

public static void getResult(String rankYear) throws ClientProtocolException, IOException{
    //3/4/1/5/2/6
    //欧洲/南美洲/亚洲/北美洲/非洲/大洋洲
     String[] land = new String[7] ;
     land[0]="";
     land[1]="亚洲";
     land[2]="非洲";
     land[3]="欧洲";
     land[4]="南美洲";
     land[5]="北美洲";
     land[6]="大洋洲";
	 Httphandle myHandle = new Httphandle();
     String urlString = "http://app.gooooal.com/teamWorldRank.do?lang=tr";
     Map<String,String> mp = new HashMap<String,String>();
     int areaId = 1;
     while(areaId<=6){
    	 int pageNo = 1;
    	 while(pageNo<=2){
		     mp.put("areaId",""+areaId);
		     mp.put("pageNo",""+pageNo);
		     mp.put("rankMonth","6");
		     mp.put("rankYear",rankYear);
		     String result = myHandle.post(urlString,mp);
		     for (int i = 2; i <= 51; i++) {
				  String titleXpath = "/html/body/div[@id='doc']/div[@id='bd']/div[@id='main_02']/form/div[@class='input']/div[@class='content']/table[@class='dataSheet']/tbody/tr["+i+"]/td[3]/a[1]";
				  String scoreXpath = "/html/body/div[@id='doc']/div[@id='bd']/div[@id='main_02']/form/div[@class='input']/div[@class='content']/table[@class='dataSheet']/tbody/tr["+i+"]/td[7]";
			      String title = new Xpath(result, titleXpath).getFilterHtmlResult();
			      String score = new Xpath(result,scoreXpath).getFilterHtmlResult();
			      if(title==null)
			    	  break;
			      System.out.println(rankYear+","+title+","+score+","+land[areaId]);
		     }
		     pageNo++;
    	 }
    	 areaId++;
     }
}
	
	
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
     
    

       
      for (int i =2014; i <=2014; i++) {
    	  String t = ""+i;
		  getResult(t);
      }
     
      
   }
   
}
