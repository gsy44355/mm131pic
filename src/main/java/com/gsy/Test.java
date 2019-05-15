package com.gsy;

import com.gsy.util.WebCrawlerUtil;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created By Gsy on 2019/5/10
 */
public class Test {
    public static void main(String[] args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("web");
        System.out.println(resourceBundle.getString("firstPage"));
        String s ="http://img1.mm131.me/pic/4697/0.jpg";

//        System.out.println(s.matches("(?<=http:.*?)0\\.jpg"));
//        s = s.replace("(.*?)0(\\.jpg)","$1"+"44"+"$2");
//        String s2 = "610404199603112012";
//        System.out.println(s2.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
//        System.out.println(s.replaceAll("(.*?)0(\\.jpg)","$133$2"));
//        String s3 = "18117835413";
//        System.out.println( s3.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
//        Map<String,String> map = WebCrawlerUtil.getMm131HtmlHeadersMap();
//        String string = WebCrawlerUtil.getWebHtml("http://www.mm131.com/qingchun/",WebCrawlerUtil.getMm131HtmlHeadersMap(),"gb2312");
        String string = WebCrawlerUtil.getWebHtml("http://www.mm131.com/qingchun/",WebCrawlerUtil.getMm131HtmlHeadersMap(),"gb2312");

        System.out.println(string);
//        string = WebCrawlerUtil.getWebHtml("https://stackoverflow.com/","utf-8");
//        System.out.println(string);
//        string = WebCrawlerUtil.getHtml("http://www.mm131.com/qingchun/",WebCrawlerUtil.getMm131HtmlHeadersMap(),"gb2312");
//        System.out.println(string);

        try {
            WebCrawlerUtil.getWebPicture("http://img1.mm131.me/pic/3928/18.jpg","1.jpg",WebCrawlerUtil.getMm131PicHeadersMap(),"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
