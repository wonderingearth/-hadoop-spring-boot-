package com.test.group_project.JDBC.domain.po;

import java.io.Serializable;

public class type implements Serializable {
    private Integer movie_id;
    private String movie_type;

    public type(Integer movie_id, String movie_type) {
        this.movie_id = movie_id;
        this.movie_type = movie_type;
    }

    public type() {
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_type() {
        return movie_type;
    }

    public void setMovie_type(String movie_type) {
        this.movie_type = movie_type;
    }

    @Override
    public String toString() {
        return "type{" +
                "movie_id=" + movie_id +
                ", movie_type='" + movie_type + '\'' +
                '}';
    }
}
