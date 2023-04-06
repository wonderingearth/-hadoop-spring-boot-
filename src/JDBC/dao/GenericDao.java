package com.test.group_project.JDBC.dao;

import java.io.Serializable;

public interface GenericDao <Entity extends Serializable, ID extends Object>{
    int insert(Entity entity);
}
