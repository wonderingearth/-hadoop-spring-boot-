package com.test.group_project.Spider.FileIO.FileIOImpl;

import com.test.group_project.Spider.FileIO.WriteFile;

import java.io.*;

public class WriteTxtFile implements WriteFile {
    private String filePath;
    File file;
    public WriteTxtFile() {
    }
    public WriteTxtFile(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void CreateFile() {
        if(filePath.indexOf("json")!=-1) {
            file = new File(filePath);
            if(file.exists()) {
                file.delete();
                file = new File(filePath);
            }
        }
        else
            System.out.println("please create a json file");
    }

    public void WriteToFile(String ...contents) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file,true),"gbk");
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        for(String content:contents) {
            bufferedWriter.write(content );
            bufferedWriter.write('\n');
            bufferedWriter.flush();
        }
        bufferedWriter.flush();
    }
}

