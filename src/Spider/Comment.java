package com.test.group_project.Spider;

public class Comment {
    private String star;
    private String content;

    @Override
    public String toString() {
        return "Comment{" +
                "star='" + star + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment(String star, String content) {
        this.star = star;
        this.content = content;
    }

    public Comment() {
    }
}
