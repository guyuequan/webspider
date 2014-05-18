package com.hq.parser.website;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.Xpath;

/**
 * spider: author cast actor
 * @author huqian.hq
 *
 */
public class tiebaComic implements ParserRule{

	@Override
	public List<String> parser1(String contentString, String urlString) {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<>();
		int odd = 1,even = 1;
		
		while(odd<=39&&even<=20){
			for (int cols = 1; cols < 6; cols++) {
				String xpath1 = "/html/body/div[@id='container']/div[@class='minwidth']/div[@class='layout_wrap']/div[@class='col-main']/div[@class='main-wrap']/div[@id='dir_content_main']/div[@class='sub_dir_box']/table/tbody/tr["+odd+"]/td["+cols+"]/a";// 1 3 5 tr
				String myXpath1 = new Xpath(contentString, xpath1).getResult();
				String myXpath2 = new Xpath(contentString, xpath1).getFilterHtmlResult();
				java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<=(href=\")).*?(?=\")");
				if(myXpath2!=""&&myXpath2!=null){
					Matcher m = p.matcher(myXpath1); 
					m.find();
					result.add(myXpath2+","+m.group());	
				}else {
					break;
				}	
			}
			odd+=1;
		
		}
		return result;
		
	}

	@Override
	public List<String> parser2(String contentString, String urlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser3(String contentString, String urlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser4(String contentString, String urlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser5(String contentString, String urlString) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
