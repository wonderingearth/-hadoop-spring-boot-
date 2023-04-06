package com.test.group_project.JDBC.dao.jdbcimpl;

import com.test.group_project.JDBC.dao.movie_allDao;
import com.test.group_project.JDBC.domain.po.movie_all;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class movie_allDaoImpl extends GenericBaseDao implements movie_allDao {
    @Override
    public int insert(movie_all ma) {
        int res = -1;
        try {
            this.getConnection();
            String sql = "insert into movie_all values(?, ?, ?, ?, ?)";
            this.executeUpdate(sql, ma.getMovie_id(), ma.getMovie_name(), ma.getMovie_types(), ma.getMovie_rate(), ma.getMovie_hotkeys());
            res = result;
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public movie_all select(String movie_name) {
        movie_all ma = null;
        try {
            this.getConnection();
            String sql = "select * from movie_all where movie_name = ?";
            this.executeQuery(sql, movie_name);
            if (rs.next()){
                ma = new movie_all(rs.getInt("movie_id"),rs.getString("movie_name"),
                        rs.getString("movie_types"),rs.getDouble("movie_rate"),
                        rs.getString("movie_hotkeys"));
            }
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ma;
    }
}
