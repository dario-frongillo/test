package it.italiancoders.mybudget.dao.test.impl;

import it.italiancoders.mybudget.dao.test.TestDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TestDaoImpl extends SqlSessionDaoSupport implements TestDao{


    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public Map<String, Object> findTest(Map<String, Object> params) {
        return getSqlSession().selectOne("it.italiancoders.mybudget.dao.Test.findTest");

    }
}
