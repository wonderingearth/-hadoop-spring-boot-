package com.test.group_project.JDBC.dao.jdbcimpl;

import com.test.group_project.JDBC.dao.commentDao;
import com.test.group_project.JDBC.domain.po.comment;

import java.sql.SQLException;

public class commentDaoImpl extends GenericBaseDao implements commentDao {
    @Override
    public int insert(comment comment) {
        int res = -1;
        try {
            this.getConnection();
            String sql = "insert into comment values(?, ?, ?, ?)";
            this.executeUpdate(sql,comment.getMovie_id(),comment.getComment_id(), comment.getComment_star(),comment.getComment_content());
            res = result;
            this.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }
}
