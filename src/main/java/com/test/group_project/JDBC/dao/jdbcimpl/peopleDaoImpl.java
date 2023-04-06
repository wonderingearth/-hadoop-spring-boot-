package com.test.group_project.JDBC.dao.jdbcimpl;

import com.test.group_project.JDBC.dao.peopleDao;
import com.test.group_project.JDBC.domain.po.people;

import java.sql.SQLException;

public class peopleDaoImpl extends GenericBaseDao implements peopleDao {
    @Override
    public int insert(people peo) {
        int res = -1;
        try {
            this.getConnection();
            String sql = "insert into people values(?, ?, ?, ?)";
            this.executeUpdate(sql, peo.getPeople_id(),peo.getPeople_name(),peo.getPeople_type(),peo.getPeople_rate());
            this.executeQuery("SELECT LAST_INSERT_ID() as people_id");
            if (rs.next())
                res = rs.getInt("people_id");
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public int update_rate(Integer people_id,Double rate) {
        int res = -1;
        try {
            this.getConnection();
            String sql = "update people set people_rate = ? where people_id = ?";
            this.executeUpdate(sql,rate, people_id);
            res = result;
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }
}
