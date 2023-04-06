package com.test.group_project.JDBC.Controller;

import com.test.group_project.JDBC.dao.jdbcimpl.movie_allDaoImpl;
import com.test.group_project.JDBC.dao.movie_allDao;
import com.test.group_project.JDBC.domain.po.movie_all;

import java.util.List;

public class Movie_allController {
    private movie_allDao movie_alldao = new movie_allDaoImpl();
    private movie_all ma;
    public int insert(){
        return movie_alldao.insert(ma);
    }
    public movie_all select(String movie_name){
        return movie_alldao.select(movie_name);
    }
    public movie_all getMa() {
        return ma;
    }

    public void setMa(movie_all ma) {
        this.ma = ma;
    }
}
