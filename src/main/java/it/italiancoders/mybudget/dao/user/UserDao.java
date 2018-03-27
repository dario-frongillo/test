package it.italiancoders.mybudget.dao.user;

import it.italiancoders.mybudget.model.api.User;

public interface UserDao {

    User findByUsernameCaseInsensitive(String username);
    Integer updateUser(User user);
    void insertUser(User user);
}
