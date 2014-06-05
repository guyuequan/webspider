package com.hq.parser.website;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hq.spider.parser.ParserRule;
import com.hq.spider.util.SpiderConfig;

public class WeiboParser implements ParserRule {

	@Override
	public List<String> parser1(String contentString, String inputString) {
		// TODO Auto-generated method stub

		List<String> resultList = new ArrayList<String>();
		contentString = contentString.replaceAll("\\\\", "");
		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile("(?<=(href=\")).*?(?=\")");
		if (contentString != null) {
			Matcher m = p.matcher(contentString);
			while (m.find()) {
				String tmp = m.group();
				if (!tmp.contains("javascript")) {
					System.out.println("------" + tmp);
					resultList.add(tmp);
				}
			}
		}
		return resultList;
	}

	@Override
	public List<String> parser2(String contentString, String inputString) {
		List<String> resultList = new ArrayList<String>();
		Document htmlDoc = Jsoup.parse(contentString);
		Elements elements = htmlDoc.getElementsByTag("script");
		Element first = elements.first();
		String content = first.html();
		int firstIndex = content.indexOf("fid");
		int endIndex = content.indexOf("ad_afr");
		String containerId = content.substring(firstIndex + 5, endIndex - 2);

		String baseUrl = "http://m.weibo.cn/page/json?containerid="
				+ containerId
				+ "_-_WEIBO_SECOND_PROFILE_WEIBO&rl=2&luicode=10000011&lfid="
				+ containerId + "&uicode=10000012&fid=" + containerId
				+ "_-_WEIBO_SECOND_PROFILE_WEIBO&ext=sourceType%3A&page=";

		int pages = 1;
		for (int i = 1; i <=pages; i++) {
			System.out.println(baseUrl + i);
			resultList.add(baseUrl + i);
		}
		return resultList;
	}

	@Override
	public List<String> parser3(String contentString, String inputString) {
		List<String> resultList = new ArrayList<>();
		try {
			JSONObject resultObj = new JSONObject(contentString);
			JSONArray resultArray1 = (JSONArray) resultObj.get("mblogList");

			int length = resultArray1.length();
			for (int i = 0; i < length; i++) {

				JSONObject obj = (JSONObject) resultArray1.get(i);
				String mid = obj.getString("mid");
				String text = obj.getString("text");
				long time = obj.getLong("created_timestamp");
//
//				HttpClient client = new DefaultHttpClient();
//				String url1 = "http://titanspider.sinaapp.com/read.php?mid=" + mid;
//				String url2 = "http://titanspider.sinaapp.com/write.php?mid=" + mid
//						+ "&value=" + time;
//
//				// 执行get请求
//				HttpGet httpGet = new HttpGet(url1);
//				HttpResponse response;
//				try {
//					response = client.execute(httpGet);
//					HttpEntity entity = response.getEntity();
//					if (entity != null) {
//						// 转化为文本信息, 设置爬取网页的字符集，防止乱码
//						String content = EntityUtils.toString(entity, "UTF-8");
//						System.out.println("content:" + content);
//
//						JSONObject jsonArray = new JSONObject(content);
//						String code = jsonArray.getString("code");
//
//					}
//
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				// 执行get请求
//				HttpGet httpGet1 = new HttpGet(url2);
//				HttpResponse response1;
//				try {
//
//					response1 = client.execute(httpGet1);
//					HttpEntity entity1 = response1.getEntity();
//					if (entity1 != null) {
//						// 转化为文本信息, 设置爬取网页的字符集，防止乱码
//						String content1 = EntityUtils.toString(entity1, "UTF-8");
//						System.out.println("content1:" + content1);
//						JSONObject jsonArray1;
//						jsonArray1 = new JSONObject(content1);
//						String code1 = jsonArray1.getString("code");	
//					}
//
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				if (obj.has("retweeted_status")) {
					JSONObject obj1;
					obj1 = (JSONObject) obj.get("retweeted_status");
					String text1 = obj1.getString("text");
					text += text1;
				}

				//
				// Pattern pattern = Pattern.compile("<img(.*)\\>\\(.*)");
				//
				// Matcher matcher = pattern.matcher(text);
				// if(matcher.matches()){
				// System.out.println("match：" + matcher.group(0));
				// String fn = matcher.group(1) ;
				// String input1 = matcher.group(2);
				// String input2 = matcher.group(3);
				// String output1 = matcher.group(4);
				// String output2 = matcher.group(5);
				// return this.doEval(fn, input1, input2, output1, output2) ;
				// }

				// String tt =
				// "<img height=\"95\" src=\"/images/srpr/logo11w.png\" width=\"269\" alt=\"Google\" >lalla";
//				String regEx = "<(img)[^>]*>";
//				Pattern pat = Pattern.compile(regEx);
//				Matcher mat = pat.matcher(text);
//				// tt=mat.replaceAll("");
//				text = mat.replaceAll("");
//				// System.out.println(tt);
//
//				// text += "http://www.jb51.net/article/16829.htm";
//
//				String regEx1 = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
//				Pattern pat1 = Pattern.compile(regEx1);
//				Matcher mat1 = pat1.matcher(text);
//				// tt=mat.replaceAll("");
//				text = mat1.replaceAll("");
//				// System.out.println(tt);

				String tmp = mid+SpiderConfig.SPLIT_STRING+htmlRemoveTag(text)+SpiderConfig.SPLIT_STRING+time;
				
				System.out.println("***********************************************************************");
				//System.out.println(htmlRemoveTag(text));
				//String tmp1 = new String(tmp.getBytes(),"uft-8");
				System.out.println("i:" + i +"----"+ tmp);
				resultList.add(tmp);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultList;
	}

	@Override
	public List<String> parser4(String contentString, String inputString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parser5(String contentString, String inputString) {
		// TODO Auto-generated method stub
		return null;
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
		inputString = inputString.replaceAll("\t", " ");
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
			//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
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
