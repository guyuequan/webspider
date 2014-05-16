webspider
=========

由于存在一些需求经常需要抓取一些detail页面，但是detail页的url一般也要通过爬取到它父子页面获得。
因此这样会形成一个层次结构，即：先通过入口url获得---子url --子url ---detail url ---最终结果。
为此，开发按层次遍历的web爬虫，输入入口为原始url，需要配置每层的解析规则，输出为抓取结果。
该爬虫能够支持多线程、高扩展！
输入： 入口url + 层数+ 每层解析规则
输出： 详情页所需要的结果



使用说明
====================
1  配置解析规则器
---------------------

具体可以查看website 里面的例子
实现一个ParserRule的 接口：  class test implements ParserRule{}
该接口会包含五个层次，即对应的每一个层次的输入：contentString，该层次的urlString
下一个层级爬虫的输入为上一个层级爬虫的输出，依次类推
你的爬虫有几个层次，则实现不同层次的解析规则

2  调用
----------------------

具体参见  test.java下调用的例子,如抓取 douban
	 public static void douban(){
		String enterUrl = "http://movie.douban.com/tag/%E6%97%A5%E6%9C%AC%E5%8A%A8%E7%94%BB";
		Parserrule doubanComicRule = new doubanComic();
		Spider mySpider = new Spider(enterUrl, "d:/douban.txt",doubanComicRule,3);
		mySpider.process();
	 }

3 扩展
-----------------------
为了便于扩展，还需要改进的一些功能

> * 现在的解析器是人为的进行添加,不够方便，以后做成直接输入xpath和正则，就能得到结果
> * 不支持分布式
> * 输出的格式现在限于文件，可以做成Monodb

