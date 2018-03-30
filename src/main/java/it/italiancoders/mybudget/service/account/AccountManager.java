package it.italiancoders.mybudget.service.account;

import it.italiancoders.mybudget.model.api.Movement;
import it.italiancoders.mybudget.model.api.User;

public interface AccountManager {
    void insertMovement(Movement movement, User user);
}
