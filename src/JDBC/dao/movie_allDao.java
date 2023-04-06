package com.test.group_project.JDBC.dao;

import com.test.group_project.JDBC.domain.po.movie;
import com.test.group_project.JDBC.domain.po.movie_all;

import java.util.List;

public interface movie_allDao extends GenericDao<movie_all,Integer>{
    movie_all select(String movie_name);
}
