package com.hq.spider.parser;

import java.util.List;

/**
 * 解析规则的接口，包括多个不同的层
 * @author huqian.hq
 *
 */
public interface Parserrule {

	
	//顶层解析器,输入为 url + url 得到的结果
	public List<String>  parser1 (String contentString,String urlString);
	
	//第二层
	public List<String>  parser2 (String contentString,String urlString);

	//第三层
	public List<String>  parser3 (String contentString,String urlString);

	//第四层
	public List<String>  parser4 (String contentString,String urlString);

	//第五层
	public List<String>  parser5 (String contentString,String urlString);
	
	

}
