package com.test.group_project.JDBC.Controller;

import com.test.group_project.JDBC.dao.commentDao;
import com.test.group_project.JDBC.dao.jdbcimpl.commentDaoImpl;
import com.test.group_project.JDBC.domain.po.comment;

public class CommentController {
    private commentDao commentdao = new commentDaoImpl();
    private comment com;
    public int insert(){
        return commentdao.insert(com);
    }
    public void setCom(comment com) {
        this.com = com;
    }

    public comment getCom() {
        return com;
    }
}
