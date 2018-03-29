package it.italiancoders.mybudget.controller.accont;

import it.italiancoders.mybudget.dao.account.AccountDao;
import it.italiancoders.mybudget.dao.category.CategoryDao;
import it.italiancoders.mybudget.model.api.*;
import it.italiancoders.mybudget.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    CategoryDao categoryDao;


    @RequestMapping(value = "protected/v1/accounts/{accountId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSellSideMembers(@PathVariable  String accountId) throws Exception {

        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Account myAccount = accountDao.findById(accountId, currentUser.getUsername());

        if(myAccount == null){
            return ResponseEntity.notFound().build();
        }


        //FAKE
        List<Category> categories = categoryDao.findCategories(accountId);

        List<Category> spese = categories.stream().filter(category -> category.getType() == MovementType.Expense).collect(Collectors.toList());

        Movement m1 = Movement.newBuilder()
                        .movementType(MovementType.Expense)
                        .amount(-50.30D)
                        .executedBy(currentUser)
                        .executedAt(DateUtils.getUnixTime(new Date()))
                        .uptadedAt(DateUtils.getUnixTime(new Date()))
                        .id(UUID.randomUUID().toString())
                         .note("Nota1")
                        .category(spese.get(0))
                        .build();

        Movement m2 = Movement.newBuilder()
                .movementType(MovementType.Expense)
                .amount(-10.30D)
                .executedBy(currentUser)
                .executedAt(DateUtils.getUnixTime(new Date()))
                .uptadedAt(DateUtils.getUnixTime(new Date()))
                .id(UUID.randomUUID().toString())
                .category(spese.get(1))
                .build();

        AccountDetails retval =new AccountDetails(myAccount);

        retval.setCategoriesAvalaible(categories);
        retval.setTotalMonthlyExpense(60.60D);
        retval.setTotalMonthlyIncoming(1000.0D);
        retval.setLastMovements(Arrays.asList(new Movement[]{m1,m2}));

        return ResponseEntity.ok(retval);
    }

}
