package com.hq.spider.parser;

import java.util.List;

/**
 * ��������Ľӿڣ����������ͬ�Ĳ�
 * @author huqian.hq
 *
 */
public interface Parserrule {

	
	//���������,����Ϊ url + url �õ��Ľ��
	public List<String>  parser1 (String contentString,String urlString);
	
	//�ڶ���
	public List<String>  parser2 (String contentString,String urlString);

	//������
	public List<String>  parser3 (String contentString,String urlString);

	//���Ĳ�
	public List<String>  parser4 (String contentString,String urlString);

	//�����
	public List<String>  parser5 (String contentString,String urlString);
	
	

}
