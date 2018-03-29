package it.italiancoders.mybudget.model.api;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetails  extends Account{


    private List<Category> categoriesAvalaible;

    private Double totalMonthlyIncoming;

    private Double totalMonthlyExpense;

    private List<Movement> lastMovements;



    public AccountDetails(Account account){
        this.setId(account.getId());
        this.setName(account.getName());
        this.setDescription(account.getDescription());
        this.setIsSharable(account.getIsSharable());
        this.setIsDeletable(account.getIsDeletable());
        this.setStatus(account.getStatus());
        this.setDailyBalance(account.getDailyBalance());
        this.setWeeklyBalance(account.getWeeklyBalance());
        this.setDefaultUsername(account.getDefaultUsername());

    }
    @Builder(builderMethodName = "newBuilderExt")
    public AccountDetails(@NotNull String id, @NotNull String name, String description, Boolean isSharable, Boolean isDeletable, AccountStatusEnum status, Double dailyBalance, Double weeklyBalance, String defaultUsername, List<Category> categoriesAvalaible, Double totalMonthlyIncoming, Double totalMonthlyExpense, List<Movement> lastMovements) {
        super(id, name, description, isSharable, isDeletable, status, dailyBalance, weeklyBalance, defaultUsername);
        this.categoriesAvalaible = categoriesAvalaible;
        this.totalMonthlyIncoming = totalMonthlyIncoming;
        this.totalMonthlyExpense = totalMonthlyExpense;
        this.lastMovements = lastMovements;
    }
}
