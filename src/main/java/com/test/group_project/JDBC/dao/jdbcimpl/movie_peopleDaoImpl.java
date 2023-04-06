package com.test.group_project.JDBC.dao.jdbcimpl;

import com.test.group_project.JDBC.dao.movie_peopleDao;
import com.test.group_project.JDBC.domain.po.movie_people;

import java.sql.SQLException;

public class movie_peopleDaoImpl extends GenericBaseDao implements movie_peopleDao {
    @Override
    public int insert(movie_people mp) {
        int res = -1;
        try {
            this.getConnection();
            String sql = "insert into movie_people values(?, ?)";
            this.executeUpdate(sql, mp.getMovie_id(),mp.getPeople_id());
            res = result;
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }
}
