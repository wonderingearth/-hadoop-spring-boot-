package com.test.group_project.JDBC.domain.po;

import java.io.Serializable;
public class comment implements Serializable {
    private Integer movie_id;
    private Integer comment_id;
    private Long comment_star;
    private String comment_content;

    public comment(Integer movie_id, Integer comment_id, Long comment_star, String comment_content) {
        this.movie_id = movie_id;
        this.comment_id = comment_id;
        this.comment_star = comment_star;
        this.comment_content = comment_content;
    }

    public comment() {
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Long getComment_star() {
        return comment_star;
    }

    public void setComment_star(Long comment_star) {
        this.comment_star = comment_star;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    @Override
    public String toString() {
        return "comment{" +
                "movie_id=" + movie_id +
                ", comment_id=" + comment_id +
                ", comment_star=" + comment_star +
                ", comment_content='" + comment_content + '\'' +
                '}';
    }
}
