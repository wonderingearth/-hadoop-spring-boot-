package com.test.group_project.JDBC.domain.po;

import java.io.Serializable;

public class movie implements Serializable {
    private Integer movie_id;

    private String movie_name;

    public movie() {
    }

    public movie(Integer movie_id, String movie_name) {
        this.movie_id = movie_id;
        this.movie_name = movie_name;
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    @Override
    public String toString() {
        return "movie{" +
                "movie_id=" + movie_id +
                ", movie_name='" + movie_name + '\'' +
                '}';
    }
}