package it.italiancoders.mybudget.controller.account;

import it.italiancoders.mybudget.dao.account.AccountDao;
import it.italiancoders.mybudget.dao.category.CategoryDao;
import it.italiancoders.mybudget.dao.movement.MovementDao;
import it.italiancoders.mybudget.exception.NoSuchEntityException;
import it.italiancoders.mybudget.exception.RestException;
import it.italiancoders.mybudget.exception.error.ErrorDetail;
import it.italiancoders.mybudget.model.api.*;
import it.italiancoders.mybudget.model.api.mybatis.MovementSummaryResultType;
import it.italiancoders.mybudget.service.account.AccountManager;
import it.italiancoders.mybudget.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    AccountManager accountManager;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MovementDao movementDao;


    @RequestMapping(value = "protected/v1/accounts/{accountId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAccount(@PathVariable  String accountId) throws Exception {

        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        Locale locale = LocaleContextHolder.getLocale();

        User currentUser = (User) auth.getPrincipal();

        Account myAccount = accountDao.findById(accountId, currentUser.getUsername());

        if(myAccount == null){
            throw new NoSuchEntityException(messageSource.getMessage("Generic.notFound",new Object[]{"Account"}, locale));
        }


        List<Category> categories = categoryDao.findCategories(accountId);
        List<Category> spese = categories.stream().filter(category -> category.getType() == MovementType.Expense).collect(Collectors.toList());
        List<Category> entrate = categories.stream().filter(category -> category.getType() == MovementType.Incoming).collect(Collectors.toList());

        List<MovementSummaryResultType> movementSummaryResultTypes = movementDao.calculateSummaryMovements(accountId, new Date());

        Map<String, Double> expenseMap = new HashMap<>();
        Map<String, Double> incomingMap = new HashMap<>();

        Double totalIncoming = 0.D;
        Double totalExpense = 0.D;

        if (movementSummaryResultTypes != null && movementSummaryResultTypes.size() > 0){
            totalIncoming = movementSummaryResultTypes.stream()
                                .filter(movementSummaryResultType -> movementSummaryResultType.getType() == MovementType.Incoming)
                                .map(movementSummaryResultType -> movementSummaryResultType.getTotal())
                                .reduce(0.0D, Double::sum);

            totalExpense = movementSummaryResultTypes.stream()
                    .filter(movementSummaryResultType -> movementSummaryResultType.getType() == MovementType.Expense)
                    .map(movementSummaryResultType -> movementSummaryResultType.getTotal())
                    .reduce(0.0D, Double::sum);

            movementSummaryResultTypes.stream()
                    .filter(movementSummaryResultType -> movementSummaryResultType.getType() == MovementType.Incoming)
                    .forEach(movementSummaryResultType -> {
                        incomingMap.put(movementSummaryResultType.getCategoryId(),movementSummaryResultType.getTotal());
                    });

            movementSummaryResultTypes.stream()
                    .filter(movementSummaryResultType -> movementSummaryResultType.getType() == MovementType.Expense)
                    .forEach(movementSummaryResultType -> {
                        expenseMap.put(movementSummaryResultType.getCategoryId(),movementSummaryResultType.getTotal());
                    });
        }

        expenseMap.put(spese.get(0).getId(),100.30D );
        expenseMap.put(spese.get(1).getId(),50.0D );
        incomingMap.put(entrate.get(0).getId(),850.00D );


        Movement m1 = Movement.newBuilder()
                        .type(MovementType.Expense)
                        .amount(100.30D)
                        .executedBy(currentUser)
                        .executedAt(DateUtils.getUnixTime(new Date()))
                        .uptadedAt(DateUtils.getUnixTime(new Date()))
                        .id(UUID.randomUUID().toString())
                         .note("Nota1")
                        .category(spese.get(0))
                        .build();

        Movement m2 = Movement.newBuilder()
                .type(MovementType.Expense)
                .amount(50.00D)
                .executedBy(currentUser)
                .executedAt(DateUtils.getUnixTime(new Date()))
                .uptadedAt(DateUtils.getUnixTime(new Date()))
                .id(UUID.randomUUID().toString())
                .category(spese.get(1))
                .build();

        Movement m3 = Movement.newBuilder()
                .type(MovementType.Incoming)
                .amount(850.00D)
                .executedBy(currentUser)
                .executedAt(DateUtils.getUnixTime(new Date()))
                .uptadedAt(DateUtils.getUnixTime(new Date()))
                .id(UUID.randomUUID().toString())
                .category(entrate.get(0))
                .build();

        List<Movement> movements = movementDao.findLastMovements(accountId,new Date(), 10);


        AccountDetails retval =new AccountDetails(myAccount);

        retval.setExpenseCategoriesAvalaible(spese);
        retval.setIncomingCategoriesAvalaible(entrate);
        retval.setTotalMonthlyExpense(totalExpense);
        retval.setTotalMonthlyIncoming(totalIncoming);
        retval.setLastMovements(movements);
        retval.setExpenseOverviewMovement(expenseMap);
        retval.setIncomingOverviewMovement(incomingMap);
        return ResponseEntity.ok(retval);
    }


    @RequestMapping(value = "protected/v1/accounts/{accountId}/movements", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> postMovement(@PathVariable  String accountId, @RequestBody @Valid Movement movement) throws Exception {

        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Locale locale = LocaleContextHolder.getLocale();


        Account myAccount = accountDao.findById(accountId, currentUser.getUsername());

        if(myAccount == null){
            throw new NoSuchEntityException(messageSource.getMessage("Generic.notFound",new Object[]{"Account"}, locale));
        }

        if(movement.getCategory() != null && movement.getCategory().getId() != null){
            Category category = categoryDao.findCategoryByIdAndAccount(movement.getCategory().getId(), accountId);
            if(category == null){
                throw new RestException(HttpStatus.BAD_REQUEST,messageSource.getMessage("AccountController.postMovementFailed",null,locale),messageSource.getMessage("AccountController.postMovementFailed.categoryNotFound",null,locale),0);
            }
        }

        movement.setAccount(myAccount);
        accountManager.insertMovement(movement, currentUser);
        return ResponseEntity.noContent().build();
    }


}
