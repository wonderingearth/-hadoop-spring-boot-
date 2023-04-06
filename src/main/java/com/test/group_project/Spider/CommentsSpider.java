package com.test.group_project.Spider;
//爬取每部电影短评的程序，顺便解决数据插入到数据库

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.group_project.Spider.FileIO.FileIOImpl.WriteJsonFile;
import com.test.group_project.Spider.FileIO.FileIOImpl.WriteTxtFile;
import com.test.group_project.JDBC.domain.po.comment;
import com.test.group_project.JDBC.domain.po.movie;
import com.test.group_project.JDBC.domain.po.type;
import com.test.group_project.JDBC.Controller.CommentController;
import com.test.group_project.JDBC.Controller.MovieController;
import com.test.group_project.JDBC.Controller.TypeController;
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
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CommentsSpider {
    public static List<String> Types = new ArrayList<>();
    public static List<String> ProxyIps = new ArrayList<>();
    //添加类型
    public static void addTypes(){
        Types.add("剧情");Types.add("喜剧");Types.add("动作");Types.add("爱情");
        Types.add("科幻");Types.add("动画");Types.add("悬疑");Types.add("惊悚");
        Types.add("恐怖");Types.add("犯罪");Types.add("同性");Types.add("音乐");
        Types.add("歌舞");Types.add("传记");Types.add("历史");Types.add("战争");
        Types.add("西部");Types.add("奇幻");Types.add("冒险");Types.add("灾难");
        Types.add("武侠");Types.add("情色");
    }
    //添加代理ip
    public static void addProxyIps() throws IOException, InterruptedException {

        System.out.println("we are getting first 6 ProxyIps ");
        String url2 = "https://proxy.qg.net/allocate?Key=KU0G687W&Num=10";
        Connection connection = Jsoup.connect(url2).ignoreContentType(true);
        Document document = connection.get();
        Thread.sleep(1000);
        String jsonStr2 = document.select("body ").text();
        JSONObject jsonObject2 = JSON.parseObject(jsonStr2);
        JSONArray MovieInfo2 = JSON.parseArray(jsonObject2.getString("Data"));
        for(int count3 = 0;count3<MovieInfo2.size();count3++){
            JSONObject parseObject = MovieInfo2.getJSONObject(count3);
            String moreProxyIp =(String) (parseObject.getString("host"));
            ProxyIps.add(moreProxyIp);
        }
        System.out.println("Getting successfully!!!!");
    }//设置 url 的基本配置
    private static final String BASE_URL1 = "comments?start=";
    private static final String BASE_URL2 = "&limit=20&status=P&sort=new_score";
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {

        //添加代理ip
        addProxyIps();
        //添加类型
        addTypes();
        //创建随机数对象
        Random random = new Random();
        //selenium浏览器的驱动设置
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        //设置浏览器模式为无头模式,可以根据需要是否注释掉
        //chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no--sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        chromeOptions.addExtensions(new File("src/main/java/com/test/group_project/Spider/proxy.zip"));
        int proxyIpNum = random.nextInt(ProxyIps.size());
        String proxyIp = ProxyIps.get(proxyIpNum);
        chromeOptions.addArguments("--proxy-server="+proxyIp);
        //创建browser对象
        WebDriver browser = new ChromeDriver(chromeOptions);
        int flag = 0;
        int flag2 = 1;
        int noUseCount = 0;
        //第一层循环，遍历每个类型的电影
        WriteTxtFile writeTxtFile = new WriteTxtFile();
        String filePath = "MovieResults3";
        writeTxtFile.setFilePath(filePath + ".json");
        writeTxtFile.CreateFile();

        writeTxtFile.WriteToFile("[");

        for(int typeCount = 11;typeCount<21;typeCount++)
        {   //记录每个 类型 的电影 的 URl 的文件路径
            String MovieURLFilePath = Types.get(typeCount)+"/homepage"+Types.get(typeCount)+".txt";
            System.out.println(Types.get(typeCount));
            File file = new File(MovieURLFilePath);
            //创建文件IO流，读取文件
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //读取文件里的每一行，获取 每一部电影 的url
            String line = bufferedReader.readLine();
            int tempFlag = 0;
            while (line!=null&&tempFlag<=60) { //TODO
                try{
                    tempFlag++;
                    Details details = new Details();
                    //获取电影的url和电影名称
                    String moveHomepageURL = line.substring(0, line.indexOf(" "));
                    String movieName = line.substring(line.indexOf(" ") + 1);
                    System.out.println(moveHomepageURL + " " + movieName);
                    details.setName(movieName);
                    details.setType(Types.get(typeCount));
                    //爬取电影 的 导演 和演员
                    int flag3 = 0;
                    String director = "";
                    String actors = "";

                    //第二层遍历，遍里每个类型下的电影 的所有短评 测试时只取 2 页短评 正式爬取时爬取 200 页，豆瓣对未登录用户有限制
                    for (int i = 0; i <= 60; i += 20) {
                        flag = 0;
                        //创建 记录 评论内容和评论星数 的 string 类型 的 List
                        List<String> commentStars = new ArrayList<String>();
                        List<String> commentContents = new ArrayList<String>();
                        //添加为完整的url
                        String url = moveHomepageURL + BASE_URL1 + i + BASE_URL2;
                        //设置随机休眠时间
                        Thread.sleep(10 + 1 * random.nextInt(5));
                        //两个标记符，flag 用于标记代理IP是否可用， flag2 用于标记该可用代理IP使用的次数，提高可用Ip的使用次数但又不至于被封
                        //创建元素容器
                        List<WebElement> CommentContentElements = new ArrayList<>();
                        List<WebElement> CommentStarElements = new ArrayList<>();
                    /*接下来这个 While 循环非常重要，该循环是用来寻找一个可用的代理IP，并通过该代理IP进行网络访问，并保证出错后重新挑选一个
                        代理ip，在另外一个spider程序里测试成功，这个程序与这个代码块的兼容程序还有待验证
                     */
                        //先选择一个代理ip

                        while (flag == 0) {
                            if (flag2 == 0)//原来的代理ip不能用，需要重新创建一个browser
                            {
                                System.out.println("No use of this ip: " + proxyIp);
                                System.out.println("starting a new browser and ProxyIp");
                                browser.quit();
                                proxyIpNum = random.nextInt(ProxyIps.size());
                                proxyIp = ProxyIps.get(proxyIpNum);
                                chromeOptions.addArguments("--proxy-server=" + proxyIp);
                                //创建browser对象
                                browser = new ChromeDriver(chromeOptions);
                                noUseCount++;
                                if (noUseCount == 100) {
                                    Thread.sleep(180000);
                                    System.out.println("无用次数太多，休眠3分钟");
                                    noUseCount = 0;
                                }
                            }
                            Set<Cookie> cookies = new HashSet<>();
                            // try-catch代码块，处理几个异常情况
                            try {    //设置超时时间为 10s
                                browser.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                                //发送请求
                                Thread.sleep(300);
                                browser.get(url);
                                cookies = browser.manage().getCookies();
                                browser.manage().deleteAllCookies();
                                browser.manage().addCookie(cookies.iterator().next());
                                System.out.println("URL is " + url);
                                //System.out.println(browser.getPageSource());
                                //如果不抛出异常，说明ip能用，则修改flag跳出循环,并爬取相应数据
                                System.out.println("proxyIp is " + proxyIp);
                                flag = 1;
                                flag2++;
                                //如果该可用ip使用次数达到一定上限，修改该ip的标识符为不可用
                                if (flag2 > 10 + random.nextInt(10))
                                    flag2 = 0;
                                //解析，定位，提取元素
                                if (flag3 == 0) {

                                    try{
                                        director = browser.findElement(By.cssSelector("div.aside > div.indent > div > span > p:nth-child(1) > a")).getText();
                                        List<WebElement> actorWebelements = browser.findElements((By.cssSelector("div.aside > div.indent > div > span > p:nth-child(2) > a")));
                                        for (int actorCount = 0; actorCount < actorWebelements.size() - 1; actorCount++)
                                            actors += actorWebelements.get(actorCount).getText() + "/";
                                        actors += actorWebelements.get(actorWebelements.size() - 1).getText();

                                        details.setActor(actors);
                                        details.setDirector(director);
                                    }catch(Exception e){
                                        break;
                                    }
                                }

                                CommentContentElements = browser.findElements(By.cssSelector("p.comment-content > span.short "));
                                CommentStarElements = browser.findElements(By.cssSelector("div.comment > h3 > span.comment-info > span:nth-child(3)"));
                                System.out.println("length of the webElements is " + CommentContentElements.size());
                                //清除List commentContents 和 commentStars 里的数据
                                commentContents.clear();
                                commentStars.clear();
                                //存储数据
                                for (int j = 0; j < CommentContentElements.size(); j++) {
                                    //开始往里面添加
                                    commentContents.add(CommentContentElements.get(j).getText());
                                    String commentStarStr = CommentStarElements.get(j).getAttribute("class").substring(CommentStarElements.get(j).getAttribute("class").indexOf("star") + 4, CommentStarElements.get(j).getAttribute("class").indexOf("star") + 5);
                                    commentStars.add(commentStarStr);
                                }
                                //如果ip有用，但是爬到的数据为空，就换其他的ip
                                if (CommentContentElements.size() == 0) {
                                    flag = 1;
                                    flag2 = 1;
                                    ProxyIps.remove(proxyIpNum);
                                    System.out.println("this ip can be used, but there is no feedback: " + proxyIp);
                                    if (ProxyIps.size() < 2) {
                                        ProxyIps.clear();
                                        System.out.println("we are going to run out of ip. And we are getting more 10 ProxyIPs");
                                        String url2 = "https://proxy.qg.net/allocate?Key=KU0G687W&Num=10";
                                        Connection connection = Jsoup.connect(url2).ignoreContentType(true);
                                        Document document = connection.get();
                                        Thread.sleep(1000);
                                        System.out.println("Getting successfully");
                                        String jsonStr2 = document.select("body ").text();
                                        JSONObject jsonObject2 = JSON.parseObject(jsonStr2);
                                        JSONArray MovieInfo2 = JSON.parseArray(jsonObject2.getString("Data"));
                                        for (int count3 = 0; count3 < MovieInfo2.size(); count3++) {
                                            JSONObject parseObject = MovieInfo2.getJSONObject(count3);
                                            String moreProxyIp = (String) (parseObject.getString("host"));
                                            ProxyIps.add(moreProxyIp);
                                        }
                                        System.out.println("last getting url is " + url);
                                        noUseCount++;
                                        if (noUseCount == 20) {
                                            Thread.sleep(10000);
                                            System.out.println("无用次数太多，休眠3分钟");
                                            noUseCount = 0;
                                            flag = 1;
                                        }
                                    }
                                }
                            } catch (Exception e1) {
                                //抛出异常，则将无用ip从ip数组里删除，如果数组删除完了，这返回此次执行到的url，结束项目
                                //移除不可用ip
                                ProxyIps.remove(proxyIpNum);
                                System.out.println("NO use of this ip: " + proxyIp);
                                browser.quit();
                                flag = 0;
                                flag2 = 0;
                                noUseCount++;
                                if (ProxyIps.size() < 2) {
                                    ProxyIps.clear();
                                    System.out.println("we are going to run out of ip. And we are getting more 10 ProxyIPs");
                                    String url2 = "https://proxy.qg.net/allocate?Key=KU0G687W&Num=10";
                                    Connection connection = Jsoup.connect(url2).ignoreContentType(true);
                                    Document document = connection.get();
                                    Thread.sleep(1000);
                                    System.out.println("Getting successfully");
                                    String jsonStr2 = document.select("body ").text();
                                    JSONObject jsonObject2 = JSON.parseObject(jsonStr2);
                                    JSONArray MovieInfo2 = JSON.parseArray(jsonObject2.getString("Data"));
                                    for (int count3 = 0; count3 < MovieInfo2.size(); count3++) {
                                        JSONObject parseObject = MovieInfo2.getJSONObject(count3);
                                        String moreProxyIp = (String) (parseObject.getString("host"));
                                        ProxyIps.add(moreProxyIp);
                                    }
                                    System.out.println("last getting url is " + url);
                                }
                                if (noUseCount == 30) {
                                    Thread.sleep(10000);
                                    System.out.println("无用次数太多，休眠3分钟");
                                    noUseCount = 0;
                                    flag = 1;
                                }
                            }
                        }
                        System.out.println("length: " + commentContents.size());
                        for (int commentCount = 0; commentCount < commentContents.size(); commentCount++) {
                            System.out.print("评分:" + commentStars.get(commentCount) + "  ");
                            System.out.println("评论内容:" + commentContents.get(commentCount));
                            Comment comment = new Comment();
                            comment.setContent(CommentContentElements.get(commentCount).getText());
                            comment.setStar(commentStars.get(commentCount));
                            details.comments.add(comment);
                        }
                    }

                    String jsonStr = JSONObject.toJSONString(details);
                    System.out.println(jsonStr);
                    writeTxtFile.WriteToFile(jsonStr + ",");
                    line = bufferedReader.readLine();
                }catch(Exception e){
                    System.out.println("小异常，不影响大数据");
                }
            }
        }
        writeTxtFile.WriteToFile("]");
    }
}
