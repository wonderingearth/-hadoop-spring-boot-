package com.test.group_project.JDBC.Utils;

import java.io.IOException;
import java.util.Properties;

public class DBUtil {
    private static Properties prop = new Properties();
    static {
        try {
            prop.load(DBUtil.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String driver= prop.getProperty("spring.datasource.driver-class-name");
    public static String url = prop.getProperty("spring.datasource.url");
    public static String user = prop.getProperty("spring.datasource.username");
    public static String password = prop.getProperty("spring.datasource.password");
}
