package it.italiancoders.mybudget.service.account;

import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.User;

import javax.validation.Valid;

public interface AccountManager {
    void insertMovement(Movement movement, User user);

    void updateMovement(@Valid Movement movement, User currentUser);

    void deleteMovement(String movementId);
}
