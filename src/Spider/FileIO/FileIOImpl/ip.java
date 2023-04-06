package com.test.group_project.Spider.FileIO.FileIOImpl;

public class ip {
    public static void main(String[] args) {
        String baseip= "60.29.153.";
        String str = "";
        for(int i = 30;i<255;i++)
            str+=baseip+i+",";
        System.out.println(str);
    }
}
