package com.test.group_project.JDBC.domain.po;

import java.io.Serializable;

public class movie_all implements Serializable {
    Integer movie_id;
    String movie_name;
    String movie_types;
    Double movie_rate;
    String movie_hotkeys;

    public movie_all(Integer movie_id, String movie_name, String movie_types, Double movie_rate, String movie_hotkeys) {
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.movie_types = movie_types;
        this.movie_rate = movie_rate;
        this.movie_hotkeys = movie_hotkeys;
    }

    public movie_all() {
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_types() {
        return movie_types;
    }

    public void setMovie_types(String movie_types) {
        this.movie_types = movie_types;
    }

    public Double getMovie_rate() {
        return movie_rate;
    }

    public void setMovie_rate(Double movie_rate) {
        this.movie_rate = movie_rate;
    }

    public String getMovie_hotkeys() {
        return movie_hotkeys;
    }

    public void setMovie_hotkeys(String movie_hotkeys) {
        this.movie_hotkeys = movie_hotkeys;
    }

    @Override
    public String toString() {
        return "movie_all{" +
                "movie_id=" + movie_id +
                ", movie_name='" + movie_name + '\'' +
                ", movie_types='" + movie_types + '\'' +
                ", movie_rate=" + movie_rate +
                ", movie_hotkeys='" + movie_hotkeys + '\'' +
                '}';
    }
}
