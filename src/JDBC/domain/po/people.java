package com.test.group_project.JDBC.domain.po;

import java.io.Serializable;

public class people implements Serializable {
    private Integer people_id;
    private String people_name;
    private String people_type;
    private Double people_rate;

    public Integer getPeople_id() {
        return people_id;
    }

    public void setPeople_id(Integer people_id) {
        this.people_id = people_id;
    }

    public String getPeople_name() {
        return people_name;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
    }

    public String getPeople_type() {
        return people_type;
    }

    public void setPeople_type(String people_type) {
        this.people_type = people_type;
    }

    public Double getPeople_rate() {
        return people_rate;
    }

    public void setPeople_rate(Double people_rate) {
        this.people_rate = people_rate;
    }

    public people(Integer people_id, String people_name, String people_type) {
        this.people_id = people_id;
        this.people_name = people_name;
        this.people_type = people_type;
    }

    public people() {
    }

    @Override
    public String toString() {
        return "people{" +
                "people_id=" + people_id +
                ", people_name='" + people_name + '\'' +
                ", people_type='" + people_type + '\'' +
                ", people_rate=" + people_rate +
                '}';
    }
}
