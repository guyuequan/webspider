package com.hq.spider.util;

public class SpiderConfig {
	
	public static final int  SLEEP_TIME = 500;//spider config: sleep time
	
	public static final int RETRY_SLEEP_TIME= 2000;//reconnenct sleep time
	
	public static final int FILE_SLEEP_TIME = 100;//file write sleep time,wait for writing
	
	public static final int RETRY_COUNT = 3;//retry connect count
	
	public static final int THREAD_COUNT = 5;//spider thread  counts
	
	public static final String DEFAULT_CHARSET="UTF-8";//GBK

	public static final String SPLIT_STRING="@@";//spit string
	

	
	public static final String USER_AGENT_ANDROID="Mozilla/5.0 (Linux; U; Android 2.3.3; zh-cn; HTC_DesireS_S510e Build/GRI40)" +
			" AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	
	public static final String USER_AGENT_FIREFOX="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0";
	
	public static final String USER_AGENT_CHROME="Mozilla/5.0 (Windows NT 5.2) AppleWebKit/534.30 (KHTML, like Gecko)" +
			" Chrome/12.0.742.122 Safari/534.30";
	
	public static final String USER_AGENT_IPHONE="Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; zh-cn) " +
			"AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5";
	
	public static String DEFAULT_AGENT =USER_AGENT_FIREFOX;
}
