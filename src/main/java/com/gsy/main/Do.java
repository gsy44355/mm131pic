package com.gsy.main;

import com.gsy.thread.GetPicThread;
import com.gsy.thread.Record;
import com.gsy.util.WebCrawlerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created By Gsy on 2019/5/10
 */
public class Do {
    String mainUrl = "http://www.mm131.com/xinggan/list_6_@replace@.html";
    static int count = 189;
    public static void main(String[] args) {
        new Do();
    }

    public Do() {
        Record record = new Record();
        LinkedList list = record.getLinkedList();
        String html = WebCrawlerUtil.getWebHtml("http://www.mm131.com/xinggan",WebCrawlerUtil.getMm131HtmlHeadersMap(),"gb2312");
        addUrl(list,html);
        for(int i = 2;i <= count;i++){
            html = WebCrawlerUtil.getWebHtml(mainUrl.replaceAll("@replace@",""+i),WebCrawlerUtil.getMm131HtmlHeadersMap(),"gb2312");
            addUrl(list,html);
        }
        record.setLinkedList(list);
        for (int i = 0; i < 20; i++) {
            GetPicThread getPicThread = new GetPicThread(record);
            Thread thread = new Thread(getPicThread);
            thread.start();
        }
    }
    public void addUrl(List list,String html){
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("list-left public-box");
        Element element = elements.first();
        Elements elements1 = element.getElementsByTag("a");
        for (Element e:elements1) {
            String s = e.toString();
            if(s.matches("<a target=\"_blank\".*?")){
                Map<String,String> map = new HashMap<>();
                map.put("page",e.attr("href"));
                map.put("pic",e.getElementsByTag("img").first().attr("src"));
                map.put("name",e.text());
                list.add(map);
//                System.out.println(e.text());
//                System.out.println(e.attr("href"));
                System.out.println(e.getElementsByTag("img").first().attr("src"));
                System.out.println(e.text());
            }
//            System.out.println("============================================================================");
//            System.out.println(e.toString());
//            System.out.println("============================================================================");
        }
    }
}
