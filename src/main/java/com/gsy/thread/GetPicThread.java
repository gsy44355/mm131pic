package com.gsy.thread;

import com.gsy.util.WebCrawlerUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By Gsy on 2019/5/10
 */
public class GetPicThread implements Runnable {
    Map map = null;
    Record record;
    Pattern pattern = Pattern.compile("共(\\d+?)页");
    Matcher matcher;
    public  static  final  String dir = "mm131pic/";
    public GetPicThread( Record record) {
        this.record = record;
    }

    @Override
    public void run() {
        while (true) {
            map = record.getUrl();
            if (map == null){
                break;
            }
            String name = (String) map.get("name");
            String pic = (String) map.get("pic");
            System.out.println(pic);
            String page = (String) map.get("page");
            String pageHtml = WebCrawlerUtil.getWebHtml(page, WebCrawlerUtil.getMm131HtmlHeadersMap(), "gb2312");
            matcher = pattern.matcher(pageHtml);
            int pageNums = 0;
            if (matcher.find()) {
                pageNums = Integer.parseInt(matcher.group(1));
            }
            String pathdir = null;
            if (pageNums > 0) {
                System.out.println(pageNums);
                pathdir = dir + name + "/";
                File file = new File(pathdir);
                if (!file.exists()) {
                    file.mkdir();
                } else {
                    continue;
                }
            }else {
                continue;
            }
            try {
                for (int i = 1; i <= pageNums; i++) {
                    WebCrawlerUtil.getWebPicture(pic.replaceAll("(.*?)0(\\.jpg)","$1"+""+i+"$2"),i+".jpg",WebCrawlerUtil.getMm131PicHeadersMap(),pathdir);
                    System.out.println("下载图片"+name+i+".pic");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
//        String s ="http://img1.mm131.me/pic/4697/0.jpg";
//        pic.replaceAll("(.*?)0(\\.jpg)","$1"++"$2");
        }
    }
}
