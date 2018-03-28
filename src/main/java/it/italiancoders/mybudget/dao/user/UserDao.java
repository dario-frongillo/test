package it.italiancoders.mybudget.dao.user;

import it.italiancoders.mybudget.model.api.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface UserDao {

    User findByUsernameCaseInsensitive(String username);
    Integer updateUser(User user);
    void insertUser(User user);
    Boolean isAlreadyExistMail(String email);
    Boolean isAlreadyExistUsername(String username);
}
