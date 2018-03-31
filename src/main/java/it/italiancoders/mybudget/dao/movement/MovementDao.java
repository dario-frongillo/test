package it.italiancoders.mybudget.dao.movement;

import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.mybatis.MovementSummaryResultType;

import java.util.Date;
import java.util.List;

public interface MovementDao {
    void inserMovement(Movement movement);
    List<Movement> findLastMovements(String accountId, Date date, Integer limit);
    List<MovementSummaryResultType> calculateSummaryMovements(String accountId, Date date);
}
