package com.hq.spider.parser;

import java.util.List;

import com.hq.spider.util.SpiderConfig;

/**
 * interface of parser rule ,contain 5 different levels
 * @author huqian.hq
 *
 */
public interface ParserRule {

	/**
	 * 
	 * @param contentString The target(HTML page) that should be parsed with Xpath.
	 * @param inputString The result of previous parser-method. Consists of all the data which will be written into the final file. Split by {@link SpiderConfig.SPLIT_STRING}
	 * @return
	 */
	public List<String>  parser1 (String contentString,String inputString);
	
	/**
	 * 
	 * @param contentString The target(HTML page) that should be parsed with Xpath.
	 * @param inputString The result of previous parser-method. Consists of all the data which will be written into the final file. Split by {@link SpiderConfig.SPLIT_STRING}
	 * @return
	 */
	public List<String>  parser2 (String contentString,String inputString);

	/**
	 * 
	 * @param contentString The target(HTML page) that should be parsed with Xpath.
	 * @param inputString The result of previous parser-method. Consists of all the data which will be written into the final file. Split by {@link SpiderConfig.SPLIT_STRING}
	 * @return
	 */
	public List<String>  parser3 (String contentString,String inputString);

	/**
	 * 
	 * @param contentString The target(HTML page) that should be parsed with Xpath.
	 * @param inputString The result of previous parser-method. Consists of all the data which will be written into the final file. Split by {@link SpiderConfig.SPLIT_STRING}
	 * @return
	 */
	public List<String>  parser4 (String contentString,String inputString);

	/**
	 * 
	 * @param contentString The target(HTML page) that should be parsed with Xpath.
	 * @param inputString The result of previous parser-method. Consists of all the data which will be written into the final file. Split by {@link SpiderConfig.SPLIT_STRING}
	 * @return
	 */
	public List<String>  parser5 (String contentString,String inputString);
	
	

}
