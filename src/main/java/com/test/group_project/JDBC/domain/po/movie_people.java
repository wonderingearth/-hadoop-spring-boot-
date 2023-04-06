package com.test.group_project.JDBC.domain.po;

import java.io.Serializable;

public class movie_people implements Serializable {
    private Integer movie_id;
    private Integer people_id;

    public movie_people(Integer movie_id, Integer people_id) {
        this.movie_id = movie_id;
        this.people_id = people_id;
    }

    public movie_people() {
    }

    public Integer getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public Integer getPeople_id() {
        return people_id;
    }

    public void setPeople_id(Integer people_id) {
        this.people_id = people_id;
    }

    @Override
    public String toString() {
        return "movie_people{" +
                "movie_id=" + movie_id +
                ", people_id=" + people_id +
                '}';
    }
}
