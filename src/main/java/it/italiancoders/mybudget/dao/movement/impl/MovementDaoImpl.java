package it.italiancoders.mybudget.dao.movement.impl;

import it.italiancoders.mybudget.dao.movement.MovementDao;
import it.italiancoders.mybudget.model.api.Account;
import it.italiancoders.mybudget.model.api.Movement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MovementDaoImpl extends SqlSessionDaoSupport implements MovementDao {


    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }


    private Map<String, Object> toHashMap(Movement movement){
        Map<String,Object> params = new HashMap<>();
        params.put("id", movement.getId());
        params.put("type", movement.getType().getValue());
        params.put("amount", movement.getAmount());
        params.put("username", movement.getExecutedBy().getUsername());
        params.put("executedAt", movement.getExecutedAt());
        params.put("note",  movement.getNote());
        params.put("accountId", movement.getAccount().getId());
        params.put("categoryId", movement.getAccount().getId());
        return params;
    }

    @Override
    public void inserMovement(Movement movement) {
        Map<String,Object> params = toHashMap(movement);
        getSqlSession().insert("it.italiancoders.mybudget.dao.Movement.insertMovement", params);

    }
}
