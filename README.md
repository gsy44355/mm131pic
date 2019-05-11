#  mm131爬虫，Java实现。
###使用方式
1.  下载工程，同时下载另一个GsyCommonUtil工程，在GsyCommenUtil目录下mvn -install 这个主要是这个工程引用了自己工程的一些操作，不下载只是需要改一下报错就可以了
2.  修改Do.java 中    mainUrl 与 count  mainUrl应该是某个大类下的第二页，把最后2替换成@replace@就可以了，
如：http://www.mm131.com/mingxing/list_5_2.html 替换为 http://www.mm131.com/mingxing/list_5_@replace@.html
count是末页数。
3.  修改GetPicThread 中 目录文件夹。然后执行Do.java main方法
4.  每个图集一个文件夹
5.  心血来潮，Java写的爬虫，能够使用，但是不够通用。
6.  仅供学习娱乐，商用请走正规渠道。



