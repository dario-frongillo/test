package it.italiancoders.mybudget.dao.movement;

import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.mybatis.MovementSummaryResultType;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

public interface MovementDao {
    void inserMovement(Movement movement);
    Movement findMovement(String accountId, String id);
    List<Movement> findLastMovements(String accountId, Date date, Integer limit);
    List<MovementSummaryResultType> calculateSummaryMovements(String accountId, Date date);
    void updateMovement(Movement movement);
    void deleteMovement(String movementId);
}
