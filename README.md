#  mm131爬虫，Java实现。
###  使用方式
1.  下载工程，同时下载另一个GsyCommonUtil工程，在GsyCommenUtil目录下mvn -install 这个主要是这个工程引用了自己工程的一些操作，不下载只是需要改一下报错就可以了
2.  修改Do.java 中  mainUrl 与 count  mainUrl应该是某个大类下的第二页，把最后2替换成@replace@就可以了，
如：http://www.mm131.com/mingxing/list_5_2.html 替换为 http://www.mm131.com/mingxing/list_5_@replace@.html
3.  count是末页数,也要修改
3.  修改GetPicThread 中 目录文件夹。然后执行Do.java main方法
4.  每个图集一个文件夹
5.  心血来潮，Java写的爬虫，能够使用，但是不够通用。
6.  仅供学习娱乐，商用请走正规渠道。
++++++++++++++++++++++++++++++++++++++
#  2019-5-15第二次提交
这次是想下另一个分类的，刚好解决了几个问题，但懒得做配置
这个网站服务器配置了Nginx 太不稳定了。。。所以获取页面方法的设置了error重复获取
+  爬取结束后，控制台输出停止，但是线程还没有全部停止，等一下就好了

#  2019-5-15 晚
最终还是做了配置
只需要修改web.properties中那四项配置，然后运行main方法，就可以了，修改方式在前面

