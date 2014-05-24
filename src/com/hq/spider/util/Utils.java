package com.hq.spider.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.CookieList;


/**
 * 
 * @author bobsanjin
 *
 */
public class Utils {


	
    /**
     *
     * 返回�?个整数�??,这个值在start~end之间,但不包含start和end<br>
     * 请注�?:只有start小于end才能产生这样的结�?<br>
     * 如果start等于end,则返回start,相当于直接返回start或�?�end<br>
     * 如果传来的start大于end,则会抛出异常
     * @param start �?始�??
     * @param end 结束�?
     * @return 返回�?个在start和end区间的整数�??
     */
    public static int getRandomInteger(int start, int end){
        if(start > end){
            throw new RuntimeException("整数区间出现错误");
        }
        if(start == end){
            return start;
        }
        return start + (int)(Math.random() * (end - start));
    }


    
    /**
	 * 启动关联应用程序来打�?文件�? 如果指定的文件是�?个目录，则启动当前平台的文件管理器打�?它�??
	 * 
	 * @param fileName
	 *            文件路径
	 * @throws IOException
	 *             如果指定文件没有关联应用程序，或者关联应用程序无法启�?
	 */
	public static void openFile(File fileName) throws IOException {
		Desktop desktop = getDesktop();
		if (desktop != null) {
			if (desktop.isSupported(Desktop.Action.OPEN)) {
				desktop.open(fileName);
			} else {
				throw new IOException("当前平台不支持该open的Action动作!");
			}
		} else {
			throw new IOException("当前平台不支持此�?(Desktop)!");
		}
	}

	


	/**
	 * 返回当前浏览器上下文�? Desktop 实例。一些平台不支持 Desktop API <br>
	 * 可以使用 isDesktopSupported() 方法来确定是否支持当前桌面�??
	 * 
	 * @return 返回当前浏览器上下文�? Desktop 实例
	 */
	protected static Desktop getDesktop() {
		if (Desktop.isDesktopSupported()) {
			return Desktop.getDesktop();
		}
		return null;
	}
	
	
	
	
	

}
