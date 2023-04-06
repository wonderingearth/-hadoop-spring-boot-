package com.test.group_project.Spider.FileIO.FileIOImpl;

import com.alibaba.fastjson.JSONObject;
import com.test.group_project.Spider.FileIO.WriteFile;

import java.io.*;
import java.util.Arrays;

public class WriteJsonFile implements WriteFile {
    private String filePath;
    File file;
    public WriteJsonFile() {
    }
    public WriteJsonFile(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void CreateFile() {
        if(filePath.indexOf("json")!=-1)
            file = new File(filePath);
        else
            System.out.println("please create a .json file");
    }

    public void WriteToFile(String ...contents) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file,true),"gbk");
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        JSONObject result = new JSONObject();
        int count = 0;
        for (String content : contents) {
                count++;
                String str = "data" + count;
                result.put(str, content);
            }
            String writeStr = result.toJSONString();
            bufferedWriter.write(writeStr);
            bufferedWriter.flush();
            bufferedWriter.close();

    }
}
