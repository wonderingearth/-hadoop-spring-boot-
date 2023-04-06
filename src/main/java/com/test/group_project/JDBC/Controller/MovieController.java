package com.test.group_project.JDBC.Controller;

import com.test.group_project.JDBC.dao.jdbcimpl.movieDaoImpl;
import com.test.group_project.JDBC.dao.movieDao;
import com.test.group_project.JDBC.domain.po.movie;
import org.springframework.stereotype.Controller;

@Controller
public class MovieController {
    private movieDao moviedao = new movieDaoImpl();
    private movie mov;
    public int insert(){
        return moviedao.insert(mov);
    }
    public void setMov(movie mov) {
        this.mov = mov;
    }

    public movie getMov() {
        return mov;
    }
}
