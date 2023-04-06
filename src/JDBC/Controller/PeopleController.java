package com.test.group_project.JDBC.Controller;


import com.test.group_project.JDBC.dao.jdbcimpl.peopleDaoImpl;
import com.test.group_project.JDBC.dao.peopleDao;
import com.test.group_project.JDBC.domain.po.people;

public class PeopleController {
    private peopleDao peoledao = new peopleDaoImpl();
    private people peo;
    public int insert(){
        return peoledao.insert(peo);
    }
    public int update(double rate){
        return peoledao.update_rate(peo.getPeople_id(), rate);
    }

    public people getPeo() {
        return peo;
    }

    public void setPeo(people peo) {
        this.peo = peo;
    }
}
