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

    private Map<String, Object> toHashMap(User user){
        Map<String,Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("email", user.getEmail());
        params.put("firstname", user.getFirstname());
        params.put("lastname", user.getLastname());
        params.put("alias", user.getAlias());
        params.put("gender", user.getGender() == null ? null : user.getGender().getValue());
        params.put("socialType", user.getSocialType() == null ? null : user.getSocialType().getValue());
        return params;
    }


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

    @Override
    public Integer updateUser(User user) {
        Map<String,Object> params = toHashMap(user);

        return getSqlSession().update("it.italiancoders.mybudget.dao.User.updateUsers",params);
    }

    @Override
    public void insertUser(User user) {
        Map<String,Object> params = toHashMap(user);
        getSqlSession().insert("it.italiancoders.mybudget.dao.User.insertUser", params);

    }

    @Override
    public Boolean isAlreadyExistMail(String email) {
        Map<String,Object> params = new HashMap<>();
        params.put("email", email);
        return (Integer) getSqlSession().selectOne("it.italiancoders.mybudget.dao.User.checkIfExist", params) > 0 ? true : false;
    }

    @Override
    public Boolean isAlreadyExistUsername(String username) {
        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        return (Integer) getSqlSession().selectOne("it.italiancoders.mybudget.dao.User.checkIfExist", params) > 0 ? true : false;

    }
}

