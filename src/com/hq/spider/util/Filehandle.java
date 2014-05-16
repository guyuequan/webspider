package com.hq.spider.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Filehandle implements Runnable {

	
	private FileOutputStream out;
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
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (isRunning) {
				//System.out.println(isRunning);
				
				if (!queue.isEmpty()) {
					try {
						out.write((queue.poll()+"\n").getBytes());
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(100);
					//Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			//last
			while(!queue.isEmpty()){
				try {
					out.write((queue.poll()+"\n").getBytes());
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//close
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}//run  end
	
	
	public void close(){
		this.isRunning = false;
	}
}
