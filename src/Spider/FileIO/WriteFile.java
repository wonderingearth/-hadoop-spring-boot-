package com.test.group_project.Spider.FileIO;

import java.io.IOException;

public interface WriteFile {
    void CreateFile();
    void WriteToFile(String ...contents) throws IOException;
}
