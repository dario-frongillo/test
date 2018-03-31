package it.italiancoders.mybudget.dao.account.impl;

import it.italiancoders.mybudget.dao.account.AccountDao;
import it.italiancoders.mybudget.dao.user.UserDao;
import it.italiancoders.mybudget.model.api.Account;
import it.italiancoders.mybudget.model.api.Category;
import it.italiancoders.mybudget.model.api.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Repository
public class AccountDaoImpl extends SqlSessionDaoSupport implements AccountDao {

    @Autowired
    @Qualifier("errorMessageSource")
    MessageSource messageSource;


    private Map<String, Object> toHashMap(Account account){
        Map<String,Object> params = new HashMap<>();
        params.put("id", account.getId());
        params.put("name", account.getName());
        params.put("description", account.getDescription());
        params.put("is_sharable", account.getIsSharable());
        params.put("is_deletable", account.getIsDeletable());
        params.put("status",  account.getStatus() == null ? null : account.getStatus().getValue());
        params.put("defaultUsername", account.getDefaultUsername());
        return params;
    }

    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public void solveTitle(Account account){
        final Locale locale = LocaleContextHolder.getLocale();

        if( account == null || StringUtils.isEmpty(account.getDefaultUsername())){
            return ;
        }

        account.setDescription(getDefaultDescription(account.getDescription()));
        account.setName(getDefaultName(account.getName()));

    }

    private List<Account> findAccounts(Map<String,Object> params){
        List<Account> accounts = getSqlSession().selectList("it.italiancoders.mybudget.dao.Account.findAccounts",params);

        if(accounts != null && accounts.size() > 0){
            accounts.stream().filter(account -> !StringUtils.isEmpty(account.getDefaultUsername())).forEach(
                    account -> {
                        solveTitle(account);
                    }
            );
        }
        return accounts;
    }

    @Override
    public List<Account> findAccountsByUsername(String username) {
        Map<String,Object> params = new HashMap<>();
        params.put("username", username);

        return findAccounts(params);

    }

    @Override
    public void insertAccount(Account account, String username) {
        Map<String,Object> params = toHashMap(account);
        getSqlSession().insert("it.italiancoders.mybudget.dao.Account.insertAccount", params);
        params = new HashMap<>();
        params.put("username", username);
        params.put("idAccount", account.getId());
        getSqlSession().insert("it.italiancoders.mybudget.dao.Account.insertUserAccount", params);

    }

    public String getDefaultName(String key){
        String i18nKey = "Predefinito";

        Locale locale = LocaleContextHolder.getLocale();

        try{
            i18nKey = messageSource.getMessage(key,null,locale);
        }catch (Exception e){

        }

        return i18nKey;
    }

    public String getDefaultDescription (String key){
        String i18nKey = "Account  personale";

        Locale locale = LocaleContextHolder.getLocale();

        try{
            i18nKey = messageSource.getMessage(key,null,locale);
        }catch (Exception e){

        }

        return i18nKey;
    }

    @Override
    public Account findById(String id, String username) {
        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("id", id);
        List<Account> retval = findAccounts(params);
        return retval == null || retval.size() == 0 ? null : retval.get(0);
    }
}
