package it.italiancoders.mybudget.service.account.impl;

import it.italiancoders.mybudget.dao.movement.MovementDao;
import it.italiancoders.mybudget.model.api.Category;
import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.User;
import it.italiancoders.mybudget.service.account.AccountManager;
import it.italiancoders.mybudget.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AccountManagerImpl implements AccountManager {

    @Value("${movement.defaultExpense.category}")
    private String defaultExpenseCategory;

    @Value("${movement.defaultIncoming.category}")
    private String defaultIncomingCategory;

    @Autowired
    MovementDao movementDao;

    @Override
    public void insertMovement(Movement movement, User user) {
        movement.setId(UUID.randomUUID().toString());

        if(movement.getExecutedBy() == null){
            movement.setExecutedBy(user);
        }

        if(movement.getExecutedAt() == null){
            movement.setExecutedAt(DateUtils.getUnixTime(new Date()));
        }

        movement.setUptadedAt(DateUtils.getUnixTime(new Date()));

        if(movement.getCategory() == null){
            switch (movement.getType()){
                case Expense:
                    movement.setCategory(Category.newBuilder().id(defaultExpenseCategory).build());
                    break;
                case Incoming:
                    movement.setCategory(Category.newBuilder().id(defaultIncomingCategory).build());
                    break;
            }
        }

        movementDao.inserMovement(movement);

    }
}
