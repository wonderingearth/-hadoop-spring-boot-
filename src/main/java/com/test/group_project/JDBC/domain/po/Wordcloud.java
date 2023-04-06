package com.test.group_project.JDBC.domain.po;

import java.util.List;

public class Wordcloud {
    String name;
    Integer value;

    @Override
    public String toString() {
        return "Wordcloud{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Wordcloud() {
    }

    public Wordcloud(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
