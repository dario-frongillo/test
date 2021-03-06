package it.italiancoders.mybudget.service.account.impl;

import it.italiancoders.mybudget.dao.movement.MovementDao;
import it.italiancoders.mybudget.model.api.AutoMovementSettings;
import it.italiancoders.mybudget.model.api.Category;
import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.User;
import it.italiancoders.mybudget.service.account.AccountManager;
import it.italiancoders.mybudget.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
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

    @Override
    public void updateMovement(@Valid Movement movement, User currentUser) {
        if(movement.getExecutedBy() == null){
            movement.setExecutedBy(currentUser);
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

        movementDao.updateMovement(movement);

    }

    @Override
    public void deleteMovement(String movementId) {
        movementDao.deleteMovement(movementId);

    }





    @Override
    public void generateAutoMovement(Date inDate) {
        List<AutoMovementSettings> autoMovementSettingsList = movementDao.findAutoMovementToGenerate(inDate);

        if(autoMovementSettingsList == null || autoMovementSettingsList.size() == 0){
            return;
        }

        autoMovementSettingsList.forEach(autoMovementSettings -> {
            Movement movement = Movement.newBuilder()
                                    .id(UUID.randomUUID().toString())
                                    .type(autoMovementSettings.getType())
                                    .amount(autoMovementSettings.getAmount())
                                    .executedBy(autoMovementSettings.getUser())
                                    .executedAt(autoMovementSettings.getMovementDate())
                                    .account(autoMovementSettings.getAccount())
                                    .category(autoMovementSettings.getCategory())
                                    .isAuto(true)
                                    .build();

            insertAutoMovement(movement, autoMovementSettings, inDate);
        });
    }

    @Override
    @Transactional
    public void insertAutoMovement(Movement movement, AutoMovementSettings autoMovementSettings, Date execDate) {

        movementDao.inserMovement(movement);
        movementDao.setExecutedMovementSettings(autoMovementSettings, execDate);
    }
}
