package com.test.group_project.Spider;
//��ȡÿ����Ӱ�����ĳ���˳�������ݲ��뵽���ݿ�

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
    //�������
    public static void addTypes(){
        Types.add("����");Types.add("ϲ��");Types.add("����");Types.add("����");
        Types.add("�ƻ�");Types.add("����");Types.add("����");Types.add("���");
        Types.add("�ֲ�");Types.add("����");Types.add("ͬ��");Types.add("����");
        Types.add("����");Types.add("����");Types.add("��ʷ");Types.add("ս��");
        Types.add("����");Types.add("���");Types.add("ð��");Types.add("����");
        Types.add("����");Types.add("��ɫ");
    }
    //��Ӵ���ip
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
    }//���� url �Ļ�������
    private static final String BASE_URL1 = "comments?start=";
    private static final String BASE_URL2 = "&limit=20&status=P&sort=new_score";
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {

        //��Ӵ���ip
        addProxyIps();
        //�������
        addTypes();
        //�������������
        Random random = new Random();
        //selenium���������������
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        //���������ģʽΪ��ͷģʽ,���Ը�����Ҫ�Ƿ�ע�͵�
        //chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no--sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        chromeOptions.addExtensions(new File("src/main/java/com/test/group_project/Spider/proxy.zip"));
        int proxyIpNum = random.nextInt(ProxyIps.size());
        String proxyIp = ProxyIps.get(proxyIpNum);
        chromeOptions.addArguments("--proxy-server="+proxyIp);
        //����browser����
        WebDriver browser = new ChromeDriver(chromeOptions);
        int flag = 0;
        int flag2 = 1;
        int noUseCount = 0;
        //��һ��ѭ��������ÿ�����͵ĵ�Ӱ
        WriteTxtFile writeTxtFile = new WriteTxtFile();
        String filePath = "MovieResults3";
        writeTxtFile.setFilePath(filePath + ".json");
        writeTxtFile.CreateFile();

        writeTxtFile.WriteToFile("[");

        for(int typeCount = 11;typeCount<21;typeCount++)
        {   //��¼ÿ�� ���� �ĵ�Ӱ �� URl ���ļ�·��
            String MovieURLFilePath = Types.get(typeCount)+"/homepage"+Types.get(typeCount)+".txt";
            System.out.println(Types.get(typeCount));
            File file = new File(MovieURLFilePath);
            //�����ļ�IO������ȡ�ļ�
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //��ȡ�ļ����ÿһ�У���ȡ ÿһ����Ӱ ��url
            String line = bufferedReader.readLine();
            int tempFlag = 0;
            while (line!=null&&tempFlag<=60) { //TODO
                try{
                    tempFlag++;
                    Details details = new Details();
                    //��ȡ��Ӱ��url�͵�Ӱ����
                    String moveHomepageURL = line.substring(0, line.indexOf(" "));
                    String movieName = line.substring(line.indexOf(" ") + 1);
                    System.out.println(moveHomepageURL + " " + movieName);
                    details.setName(movieName);
                    details.setType(Types.get(typeCount));
                    //��ȡ��Ӱ �� ���� ����Ա
                    int flag3 = 0;
                    String director = "";
                    String actors = "";

                    //�ڶ������������ÿ�������µĵ�Ӱ �����ж��� ����ʱֻȡ 2 ҳ���� ��ʽ��ȡʱ��ȡ 200 ҳ�������δ��¼�û�������
                    for (int i = 0; i <= 60; i += 20) {
                        flag = 0;
                        //���� ��¼ �������ݺ��������� �� string ���� �� List
                        List<String> commentStars = new ArrayList<String>();
                        List<String> commentContents = new ArrayList<String>();
                        //���Ϊ������url
                        String url = moveHomepageURL + BASE_URL1 + i + BASE_URL2;
                        //�����������ʱ��
                        Thread.sleep(10 + 1 * random.nextInt(5));
                        //������Ƿ���flag ���ڱ�Ǵ���IP�Ƿ���ã� flag2 ���ڱ�Ǹÿ��ô���IPʹ�õĴ�������߿���Ip��ʹ�ô������ֲ����ڱ���
                        //����Ԫ������
                        List<WebElement> CommentContentElements = new ArrayList<>();
                        List<WebElement> CommentStarElements = new ArrayList<>();
                    /*��������� While ѭ���ǳ���Ҫ����ѭ��������Ѱ��һ�����õĴ���IP����ͨ���ô���IP����������ʣ�����֤�����������ѡһ��
                        ����ip��������һ��spider��������Գɹ��������������������ļ��ݳ����д���֤
                     */
                        //��ѡ��һ������ip

                        while (flag == 0) {
                            if (flag2 == 0)//ԭ���Ĵ���ip�����ã���Ҫ���´���һ��browser
                            {
                                System.out.println("No use of this ip: " + proxyIp);
                                System.out.println("starting a new browser and ProxyIp");
                                browser.quit();
                                proxyIpNum = random.nextInt(ProxyIps.size());
                                proxyIp = ProxyIps.get(proxyIpNum);
                                chromeOptions.addArguments("--proxy-server=" + proxyIp);
                                //����browser����
                                browser = new ChromeDriver(chromeOptions);
                                noUseCount++;
                                if (noUseCount == 100) {
                                    Thread.sleep(180000);
                                    System.out.println("���ô���̫�࣬����3����");
                                    noUseCount = 0;
                                }
                            }
                            Set<Cookie> cookies = new HashSet<>();
                            // try-catch����飬�������쳣���
                            try {    //���ó�ʱʱ��Ϊ 10s
                                browser.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                                //��������
                                Thread.sleep(300);
                                browser.get(url);
                                cookies = browser.manage().getCookies();
                                browser.manage().deleteAllCookies();
                                browser.manage().addCookie(cookies.iterator().next());
                                System.out.println("URL is " + url);
                                //System.out.println(browser.getPageSource());
                                //������׳��쳣��˵��ip���ã����޸�flag����ѭ��,����ȡ��Ӧ����
                                System.out.println("proxyIp is " + proxyIp);
                                flag = 1;
                                flag2++;
                                //����ÿ���ipʹ�ô����ﵽһ�����ޣ��޸ĸ�ip�ı�ʶ��Ϊ������
                                if (flag2 > 10 + random.nextInt(10))
                                    flag2 = 0;
                                //��������λ����ȡԪ��
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
                                //���List commentContents �� commentStars �������
                                commentContents.clear();
                                commentStars.clear();
                                //�洢����
                                for (int j = 0; j < CommentContentElements.size(); j++) {
                                    //��ʼ���������
                                    commentContents.add(CommentContentElements.get(j).getText());
                                    String commentStarStr = CommentStarElements.get(j).getAttribute("class").substring(CommentStarElements.get(j).getAttribute("class").indexOf("star") + 4, CommentStarElements.get(j).getAttribute("class").indexOf("star") + 5);
                                    commentStars.add(commentStarStr);
                                }
                                //���ip���ã���������������Ϊ�գ��ͻ�������ip
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
                                            System.out.println("���ô���̫�࣬����3����");
                                            noUseCount = 0;
                                            flag = 1;
                                        }
                                    }
                                }
                            } catch (Exception e1) {
                                //�׳��쳣��������ip��ip������ɾ�����������ɾ�����ˣ��ⷵ�ش˴�ִ�е���url��������Ŀ
                                //�Ƴ�������ip
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
                                    System.out.println("���ô���̫�࣬����3����");
                                    noUseCount = 0;
                                    flag = 1;
                                }
                            }
                        }
                        System.out.println("length: " + commentContents.size());
                        for (int commentCount = 0; commentCount < commentContents.size(); commentCount++) {
                            System.out.print("����:" + commentStars.get(commentCount) + "  ");
                            System.out.println("��������:" + commentContents.get(commentCount));
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
                    System.out.println("С�쳣����Ӱ�������");
                }
            }
        }
        writeTxtFile.WriteToFile("]");
    }
}
