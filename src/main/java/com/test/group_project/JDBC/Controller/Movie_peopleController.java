package com.test.group_project.JDBC.Controller;

import com.test.group_project.JDBC.dao.jdbcimpl.movie_peopleDaoImpl;
import com.test.group_project.JDBC.dao.movie_peopleDao;
import com.test.group_project.JDBC.domain.po.movie_people;

public class Movie_peopleController {
    private movie_peopleDao movie_peoledao = new movie_peopleDaoImpl();
    private movie_people mp;
    public int insert(){
        return movie_peoledao.insert(mp);
    }
    public movie_people getMp() {
        return mp;
    }

    public void setMp(movie_people mp) {
        this.mp = mp;
    }
}
