package it.italiancoders.mybudget.dao.category.impl;

import it.italiancoders.mybudget.dao.account.AccountDao;
import it.italiancoders.mybudget.dao.category.CategoryDao;
import it.italiancoders.mybudget.model.api.Account;
import it.italiancoders.mybudget.model.api.Category;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CategoryDaoImpl extends SqlSessionDaoSupport implements CategoryDao {

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
    public List<Category> findCategories(String accountId) {
        Map<String,Object> params = new HashMap<>();
        params.put("accountId", accountId);
        final Locale locale = LocaleContextHolder.getLocale();
        List<Category> categories = getSqlSession().selectList("it.italiancoders.mybudget.dao.Category.findCategories",params);

        if(categories == null || categories.size() == 0){
            return new ArrayList<>();
        }

        return categories.stream()
                    .map(category -> {
                        if(category.getIsEditable()){
                            return category;
                        }
                        String value = category.getValue();

                        try{
                            value = messageSource.getMessage(value,null,locale);
                        }catch (Exception e){

                        }

                        category.setValue(value);
                        return category;
                    }).sorted((c1,c2) -> c1.getValue().compareToIgnoreCase(c2.getValue()))
                    .collect(Collectors.toList());
    }
}
