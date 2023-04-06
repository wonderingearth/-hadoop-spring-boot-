package com.test.group_project.Spider;

import com.nimbusds.jose.shaded.json.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {
        file_to_db.process();
    }
}
