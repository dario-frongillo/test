package it.italiancoders.mybudget.dao.user.impl;

import it.italiancoders.mybudget.dao.test.TestDao;
import it.italiancoders.mybudget.dao.user.UserDao;
import it.italiancoders.mybudget.model.api.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {


    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }


    @Override
    public User findByUsernameCaseInsensitive(String username) {
        Map<String,Object> params = new HashMap<>();
        params.put("username", username);

        return getSqlSession().selectOne("it.italiancoders.mybudget.dao.User.findUsers",params);

    }
}

