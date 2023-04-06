package com.test.group_project.JDBC.domain.po;

public class Details {
    String movie;
    String type;
    Double value;

    @Override
    public String toString() {
        return "Details{" +
                "movie='" + movie + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Details(String movie, String type, Double value) {
        this.movie = movie;
        this.type = type;
        this.value = value;
    }
}
