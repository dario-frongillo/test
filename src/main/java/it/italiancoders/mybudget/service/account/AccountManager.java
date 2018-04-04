package it.italiancoders.mybudget.service.account;

import it.italiancoders.mybudget.model.api.AutoMovementSettings;
import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.User;

import javax.validation.Valid;
import java.util.Date;

public interface AccountManager {
    void insertMovement(Movement movement, User user);

    void updateMovement(@Valid Movement movement, User currentUser);

    void deleteMovement(String movementId);

    void generateAutoMovement(Date inDate);

    void insertAutoMovement(Movement movement, AutoMovementSettings autoMovementSettings, Date execDate);
}
