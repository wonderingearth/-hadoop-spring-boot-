package com.test.group_project.JDBC.dao;

import com.test.group_project.JDBC.domain.po.people;

public interface peopleDao extends GenericDao<people,Integer>{
    int update_rate(Integer people_id,Double rate);
}
