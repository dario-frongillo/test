package it.italiancoders.mybudget.dao.account;

import it.italiancoders.mybudget.model.api.Account;
import it.italiancoders.mybudget.model.api.User;

import java.util.List;

public interface AccountDao {
    List<Account> findAccountsByUsername(String username);

    void insertAccount(Account defaultAccount, String username);

    String getDefaultName(String key);

    String getDefaultDescription (String key);

    Account findById(String id, String username);

    void solveTitle(Account account);

    List<User> findAccountMembers(String accountId);

}
