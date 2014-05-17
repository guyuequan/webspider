package com.hq.spider.parser;

import java.util.List;

/**
 * interface of parser rule ,contain 5 different levels
 * @author huqian.hq
 *
 */
public interface Parserrule {

	
	public List<String>  parser1 (String contentString,String urlString);
	
	public List<String>  parser2 (String contentString,String urlString);

	public List<String>  parser3 (String contentString,String urlString);

	public List<String>  parser4 (String contentString,String urlString);

	public List<String>  parser5 (String contentString,String urlString);
	
	

}
