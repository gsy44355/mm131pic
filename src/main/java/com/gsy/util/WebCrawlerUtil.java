package com.gsy.util;

import com.gsy.gsy_common_util.fileUtil.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created By Gsy on 2019/5/10
 */
public class WebCrawlerUtil {
    public static boolean getWebPicture(String urlStr,String pictureName, Map<String,String> headers,String directory)throws IOException{
        InputStream inStream = null;
        FileOutputStream outStream = null;
        try{
            //new一个URL对象
            URL url = new URL(urlStr);
            System.out.println(urlStr);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String,String> entry: entrySet) {
                conn.addRequestProperty(entry.getKey(),entry.getValue());
            }
            //通过输入流获取图片数据
            inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            if(data ==null || data.length == 0){
                System.out.println(WebCrawlerUtil.class+"图片解析失败，数据为空，url={}"+urlStr);
                return false;
            }
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File(directory+pictureName);
            //创建输出流
            outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
            return true;
        }catch (IOException e){
            System.out.println(WebCrawlerUtil.class+"下载图片失败");
            e.printStackTrace();
            throw e;
        }finally {
            FileUtils.safeClose(inStream);
            FileUtils.safeClose(outStream);
        }
    }
    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = null;
        try{
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[4096];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while( (len=inStream.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //把outStream里的数据写入内存
            return outStream.toByteArray();
        }catch (IOException e){
            throw e;
        }finally {
            FileUtils.safeClose(inStream);
            FileUtils.safeClose(outStream);
        }
    }

    public static String getWebHtml(String urlStr,String encoding){
        InputStream inputStream = null;
        String string = null;
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);
            inputStream = url.openStream();

//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
//            connection.setRequestMethod("GET");
//            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
//            for (Map.Entry<String,String> entry: entrySet) {
//                connection.addRequestProperty(entry.getKey(),entry.getValue());
//            }
//            inputStream = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream, encoding));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            string = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            FileUtils.safeClose(inputStream);
            FileUtils.safeClose(br);
        }
        return string;
    }

    public static String getWebHtml(String urlStr,Map<String,String> headers,String encoding){
        InputStream inputStream = null;
        String string = null;
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);
//            inputStream = url.openStream();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String,String> entry: entrySet) {
                connection.addRequestProperty(entry.getKey(),entry.getValue());
            }
            inputStream = connection.getInputStream();
            GZIPInputStream gzis=new GZIPInputStream(inputStream);
            InputStreamReader reader = new InputStreamReader(gzis,encoding);
            br = new BufferedReader(reader);
//            br = new BufferedReader(new InputStreamReader(inputStream, encoding));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            string = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return getWebHtml(urlStr,headers,encoding);
        }finally {
            FileUtils.safeClose(inputStream);
            FileUtils.safeClose(br);
        }
        return string;
    }

    public static String getHtml(String url,Map<String,String> headers,String encoding){
        //构造Headers
        List<Header> headerList = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        for (Map.Entry<String,String> entry: entrySet) {
            headerList.add(new BasicHeader(entry.getKey(),entry.getValue()));
        }
        //构造HttpClient
        HttpClient httpClient = HttpClients.custom().setDefaultHeaders(headerList).build();
        //构造HttpGet请求
        HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(url).build();
        //获取结果
        HttpResponse httpResponse = null;
        String rawHTMLContent = null;
        try {
            httpResponse = httpClient.execute(httpUriRequest);
            //获取返回结果中的实体
            HttpEntity entity = httpResponse.getEntity();

            //查看页面内容结果
            rawHTMLContent = EntityUtils.toString(entity,encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawHTMLContent;
    }
    /**
     * 返回一个通用的headersMap供代码使用，建议自行拼装。
     * @return
     */
    public static Map<String ,String> getCommenHeadersMap(){
        Map<String,String> map = new HashMap<String, String>();
        map.put("Accept","image/webp,image/apng,image/*,*/*;q=0.8");
        map.put("Accept-Encoding","gzip, deflate");
        map.put("Language","zh-CN,zh;q=0.9,en;q=0.8");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
        map.put("Proxy-Connection","keep-alive");
        return map;
    }
    public static Map<String,String> getMm131PicHeadersMap(){
        Map<String,String> map = new HashMap<String, String>();
        map.put("Accept","image/webp,image/apng,image/*,*/*;q=0.8");
        map.put("x-forwarded-for",getRandomIp());
        map.put("Accept-Encoding","gzip, deflate");
        map.put("Language","zh-CN,zh;q=0.9,en;q=0.8");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
        map.put("Referer","http://www.mm131.com/xinggan/4939_28.html");
        map.put("Host","img1.mm131.me");
        map.put("Proxy-Connection","keep-alive");
        return map;
    }
    public static Map<String,String> getMm131HtmlHeadersMap(){
        Map<String,String> map = new HashMap<String, String>();
        map.put("Host"," www.mm131.com");
        map.put("Proxy-Connection"," keep-alive");
        map.put("x-forwarded-for",getRandomIp());
        map.put("Upgrade-Insecure-Requests","1");
        map.put("User-Agent"," Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
        map.put("Accept"," text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        map.put("Referer"," http://www.mm131.com/xinggan/list_6_9.html");
        map.put("Accept-Encoding"," gzip, deflate");
        map.put("Accept-Language"," zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Cookie"," bdshare_firstime=1557462796510; Hm_lvt_9a737a8572f89206db6e9c301695b55a=1557462797; Hm_lpvt_9a737a8572f89206db6e9c301695b55a=1557482771");
        return map;
    }
    public static String getRandomIp() {

        // ip范围
        int[][] range = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
                { 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
                { 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
                { 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
                { 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
                { -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
                { -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
                { -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
                { -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
                { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
        };

        Random rdint = new SecureRandom();
        int index = rdint.nextInt(10);
        String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
        return ip;
    }

    /*
     * 将十进制转换成IP地址
     */
    public static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }


}
