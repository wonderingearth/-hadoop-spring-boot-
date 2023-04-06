package com.test.group_project.Spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.group_project.Spider.FileIO.FileIOImpl.WriteJsonFile;
import com.test.group_project.Spider.FileIO.FileIOImpl.WriteTxtFile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MovieURLsSpiderByJson {
    private static final String BASE_URL = "https://movie.douban.com/j/new_search_subjects?sort=U&range=0,20&tags=电影&start=";
    private static final String TYPE_BASE_URL = "&genres=";
    public static List<String> Types = new ArrayList<>();
    public static List<String> ProxyIps = new ArrayList<>();
    public static void addTypes(){
        Types.add("剧情");Types.add("喜剧");Types.add("动作");Types.add("爱情");
        Types.add("科幻");Types.add("动画");Types.add("悬疑");Types.add("惊悚");
        Types.add("恐怖");Types.add("犯罪");Types.add("同性");Types.add("音乐");
        Types.add("歌舞");Types.add("传记");Types.add("历史");Types.add("战争");
        Types.add("西部");Types.add("奇幻");Types.add("冒险");Types.add("灾难");
        Types.add("武侠");Types.add("情色");
    }

    public static void addProxyIps() throws IOException, InterruptedException {
        System.out.println("we are getting 20 ProxyIps ");
        String url2 = "https://proxy.qg.net/allocate?Key=KU0G687W=20";
        Connection connection = Jsoup.connect(url2).ignoreContentType(true);
        Document document = connection.get();
        Thread.sleep(1000);
        System.out.println(document);
        String jsonStr2 = document.select("body ").text();
        JSONObject jsonObject2 = JSON.parseObject(jsonStr2);
        JSONArray MovieInfo2 = JSON.parseArray(jsonObject2.getString("Data"));
        for(int count3 = 0;count3<MovieInfo2.size();count3++){
            JSONObject parseObject = MovieInfo2.getJSONObject(count3);
            String moreProxyIp =(String) (parseObject.getString("host"));
            ProxyIps.add(moreProxyIp);
        }
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        //1. 添加类型
        MovieURLsSpiderByJson.addTypes();
        //2. 获取代理IP
        addProxyIps();
        // 3. 配置浏览器驱动
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        //设置无头模式
        //chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no--sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        chromeOptions.addExtensions(new File("src/main/java/com/test/group_project/Spider/proxy.zip"));


        Random random = new Random();
        //4. 第一层遍历，遍历所有电影类型
        for(int i = 0;i<1;i++){
            //创建文件夹以及文件
            File fileDic = new File(Types.get(i));
            if(!fileDic.exists())
                fileDic.mkdirs();
            WriteTxtFile filePiplines = new WriteTxtFile(Types.get(i)+"/"+"homePage"+Types.get(i)+".txt");
            filePiplines.CreateFile();
            WriteJsonFile jsonPiplines = new WriteJsonFile(Types.get(i)+"/"+"homePage"+Types.get(i)+".json");
            jsonPiplines.CreateFile();
            String tempProxyIp = "";
            //5. 第二层遍历，遍历每个类型下的所有电影
            int flag2 = 0;
            for(int j = 0;j<2000;j+=20){
                String url = BASE_URL+j+TYPE_BASE_URL+Types.get(i);
                Thread.sleep(10+1*random.nextInt(5));
                int flag = 0;//flag用来标志ip是否能用，过期或者被封
                WebElement webElement =null;
                String jsonStr = "";

                while(flag==0){
                    //1. 随机选取代理ip

                    int proxyIpNum = random.nextInt(ProxyIps.size());

                    String proxyIp = "";
                    if(flag2>0)
                        proxyIp = tempProxyIp;
                    else
                        proxyIp=ProxyIps.get(proxyIpNum);
                    //1. 添加代理ip
                    chromeOptions.addArguments("--proxy-server="+proxyIp);
                    WebDriver browser = new ChromeDriver(chromeOptions);
                    Set<Cookie> cookies = new HashSet<>();

                    try{    //设置超时时间为 10s
                            browser.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
                            browser.get(url);
                            cookies = browser.manage().getCookies();
                            browser.manage().deleteAllCookies();
                            browser.manage().addCookie(cookies.iterator().next());

                            //如果不抛出异常，说明ip能用，则修改flag跳出循环,并爬取相应数据
                            System.out.println(proxyIp);
                            flag = 1;
                            flag2 ++;
                            if(flag2>2+random.nextInt(4))
                                flag2=0;
                            tempProxyIp = proxyIp;
                            WebElement tempWebElement = browser.findElement(By.cssSelector("body > pre"));
                            webElement = tempWebElement;
                            jsonStr = webElement.getText();
                            if(jsonStr.indexOf("异常")!=-1) {
                                flag = 0;
                                flag2 = 0;
                                ProxyIps.remove(proxyIpNum);
                                System.out.println("banned of this ip: "+proxyIp);
                                if(ProxyIps.size()<3) {
                                    System.out.println("we are going to run out of ip. And we are getting more 20 ProxyIPs");
                                    String url2 = "https://proxy.qg.net/allocate?Key=KU0G687W&Num=20";
                                    Connection connection = Jsoup.connect(url2).ignoreContentType(true);
                                    Document document = connection.get();
                                    Thread.sleep(1000);
                                    System.out.println(document);
                                    String jsonStr2 = document.select("body ").text();
                                    JSONObject jsonObject2 = JSON.parseObject(jsonStr2);
                                    JSONArray MovieInfo2 = JSON.parseArray(jsonObject2.getString("Data"));
                                    for (int count = 0; count < MovieInfo2.size(); count++) {
                                        JSONObject parseObject = MovieInfo2.getJSONObject(count);
                                        String moreProxyIp = (String) (parseObject.getString("host"));
                                        ProxyIps.add(moreProxyIp);
                                    }
                                }
                            }
                            System.out.println(jsonStr);
                            System.out.println("IP: "+proxyIp+" url: "+url);
                            browser.quit();
                    }
                    catch(Exception e1){
                        //抛出异常，则将无用ip从ip数组里删除，如果数组删除完了，这返回此次执行到的url，结束项目
                        //移除不可用ip
                        ProxyIps.remove(proxyIpNum);
                        System.out.println("NO use of this ip: "+proxyIp);
                        browser.quit();
                        flag = 0;
                        flag2 = 0;
                        if(ProxyIps.size()<3) {
                            System.out.println("we are going to run out of ip. And we are getting more 20 ProxyIPs");
                            String url2 = "https://proxy.qg.net/allocate?Key=KU0G687W&Num=20";
                            Connection connection = Jsoup.connect(url2).ignoreContentType(true);
                            Document document = connection.get();
                            Thread.sleep(1000);
                            System.out.println(document);
                            String jsonStr2 = document.select("body ").text();
                            JSONObject jsonObject2 = JSON.parseObject(jsonStr2);
                            JSONArray MovieInfo2 = JSON.parseArray(jsonObject2.getString("Data"));
                            for(int count = 0;count<MovieInfo2.size();count++){
                                JSONObject parseObject = MovieInfo2.getJSONObject(count);
                                String moreProxyIp =(String) (parseObject.getString("host"));
                                ProxyIps.add(moreProxyIp);
                            }
                            System.out.println("last getting url is "+url);
                            browser.quit();
                        }
                    }

                }
                if(jsonStr.equals("{\"data\":[]}")&&j>1500)
                    break;
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                JSONArray MovieInfo = JSON.parseArray(jsonObject.getString("data"));

                for(int count = 0;count<MovieInfo.size();count++){
                    JSONObject parseObject = MovieInfo.getJSONObject(count);
                    String movieHomePage =(String) (parseObject.getString("url"));
                    String movieName = (String) (parseObject.getString("title"));
                    filePiplines.WriteToFile(movieHomePage,movieName);
                    jsonPiplines.WriteToFile(movieHomePage, movieName);
                }
            }
        }
    }
}
