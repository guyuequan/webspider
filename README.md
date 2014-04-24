webspider
=========

由于存在一些需求经常需要抓取一些detail页面，但是detail页的url一般也要通过爬取到它父子页面获得。
因此这样会形成一个层次结构，即：先通过入口url获得---子url --子url ---detail url ---最终结果。
为此，开发按层次遍历的web爬虫，输入入口为原始url，需要配置每层的解析规则，输出为抓取结果。
该爬虫能够支持多线程、高扩展！
输入： 入口url + 层数+ 每层解析规则
输出： 详情页所需要的结果





A First Level Header
====================
A Second Level Header
---------------------

Now is the time for all good men to come to
the aid of their country. This is just a
regular paragraph.

The quick brown fox jumped over the lazy
dog's back.
### Header 3

> This is a blockquote.
> 
> This is the second paragraph in the blockquote.
>
> ## This is an H2 in a blockquote
