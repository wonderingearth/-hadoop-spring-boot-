package com.test.group_project.JDBC.Controller;

import com.test.group_project.JDBC.dao.jdbcimpl.typeDaoImpl;
import com.test.group_project.JDBC.dao.typeDao;
import com.test.group_project.JDBC.domain.po.type;

public class TypeController {
    private typeDao typedao = new typeDaoImpl();
    private type tp;
    public int insert(){
        return typedao.insert(tp);
    }

    public type getTp() {
        return tp;
    }

    public void setTp(type tp) {
        this.tp = tp;
    }

}
