package com.test.group_project.JDBC.dao.jdbcimpl;

import com.test.group_project.JDBC.dao.movieDao;
import com.test.group_project.JDBC.domain.po.movie;

import java.sql.SQLException;

public class movieDaoImpl extends GenericBaseDao implements movieDao {
    @Override
    public int insert(movie movie) {
        int res = -1;
        try {
            this.getConnection();
            this.executeQuery("select * from movie where movie_name = ?",movie.getMovie_name());
            if (rs.next())
                return -1;
            String sql = "insert into movie values(?, ?)";
            this.executeUpdate(sql, movie.getMovie_id(),movie.getMovie_name());
            this.executeQuery("SELECT LAST_INSERT_ID() as movie_id");
            if (rs.next())
                res = rs.getInt("movie_id");
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

}
