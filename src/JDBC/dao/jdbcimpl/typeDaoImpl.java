package com.test.group_project.JDBC.dao.jdbcimpl;

import com.test.group_project.JDBC.dao.typeDao;
import com.test.group_project.JDBC.domain.po.type;

import java.sql.SQLException;

public class typeDaoImpl extends GenericBaseDao implements typeDao {
    @Override
    public int insert(type type) {
        int res = -1;
        try {
            this.getConnection();
            String sql = "insert into type values(?, ?)";
            this.executeUpdate(sql, type.getMovie_id(),type.getMovie_type());
            res=result;
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }
}
