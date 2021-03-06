package com.hq.spider.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Filehandle implements Runnable {

	
	private FileOutputStream out;
	private OutputStreamWriter writer;
	private ConcurrentLinkedQueue<String> queue;
	private String pathString;

	private  boolean isRunning = true;
	public Filehandle() {
	
	}

	public Filehandle(String pathString, ConcurrentLinkedQueue<String> outputQueue)  {
		this.queue = outputQueue;
		this.pathString = pathString;
	}

	@Override
	public void run() {
			File file=new File(pathString);
			try {
				out = new FileOutputStream(file, true);
				writer = new OutputStreamWriter(out,SpiderConfig.DEFAULT_CHARSET);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			while (isRunning) {
				
				if (!queue.isEmpty()) {
					try {
						writer.write(queue.poll()+"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(SpiderConfig.FILE_SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			//last
			while(!queue.isEmpty()){
				try {					
					writer.write(queue.poll()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//close
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}//run  end
	
	
	public void close(){
		this.isRunning = false;
	}
}
