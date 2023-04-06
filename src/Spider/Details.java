package com.test.group_project.Spider;

import java.util.ArrayList;
import java.util.List;
//本实体类用于爬虫传递数据，写为json文件使用
public class Details {
    private String type;
    private String name;
    private String director;
    private String actor;
    public List<Comment> comments = new ArrayList<>();

    public Details() {
    }

    @Override
    public String toString() {
        return "Details{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", director='" + director + '\'' +
                ", actor='" + actor + '\'' +
                ", comments=" + comments +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Details(String type, String name, String director, String actor, List<Comment> comments) {
        this.type = type;
        this.name = name;
        this.director = director;
        this.actor = actor;
        this.comments = comments;
    }
}
