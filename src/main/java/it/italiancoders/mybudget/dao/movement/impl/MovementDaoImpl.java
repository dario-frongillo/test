package it.italiancoders.mybudget.dao.movement.impl;

import it.italiancoders.mybudget.dao.account.AccountDao;
import it.italiancoders.mybudget.dao.category.CategoryDao;
import it.italiancoders.mybudget.dao.movement.MovementDao;
import it.italiancoders.mybudget.model.api.Account;
import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.mybatis.MovementSummaryResultType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MovementDaoImpl extends SqlSessionDaoSupport implements MovementDao {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    AccountDao accountDao;

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
        params.put("categoryId", movement.getCategory().getId());
        return params;
    }

    @Override
    public void inserMovement(Movement movement) {
        Map<String,Object> params = toHashMap(movement);
        getSqlSession().insert("it.italiancoders.mybudget.dao.Movement.insertMovement", params);

    }

    @Override
    public List<Movement> findLastMovements(String accountId, Date date, Integer limit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        Map<String,Object> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("month", month+1);
        params.put("year", year);
        params.put("limit", limit);

        List<Movement> retval =  getSqlSession().selectList("it.italiancoders.mybudget.dao.Movement.findMovements", params);

        retval.forEach(movement -> {
            categoryDao.solveTitle(movement.getCategory());
            accountDao.solveTitle(movement.getAccount());
        });

        return retval;
    }

    @Override
    public List<MovementSummaryResultType> calculateSummaryMovements(String accountId, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        Map<String,Object> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("month", month+1);
        params.put("year", year);


        List<MovementSummaryResultType> movementSummaryResultTypes = getSqlSession().selectList("it.italiancoders.mybudget.dao.Movement.calculateSummaryMovements", params);
        return movementSummaryResultTypes;
    }
}