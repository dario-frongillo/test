package it.italiancoders.mybudget.service.user;

import it.italiancoders.mybudget.model.api.RegistrationUser;
import it.italiancoders.mybudget.model.api.User;

public interface UserManager {
    User createUser(RegistrationUser registrationUser);
}
