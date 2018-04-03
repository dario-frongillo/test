package it.italiancoders.mybudget.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetails  extends Account{


    @JsonProperty("incomingCategoriesAvalaible")
    private List<Category> incomingCategoriesAvalaible;

    @JsonProperty("expenseCategoriesAvalaible")
    private List<Category> expenseCategoriesAvalaible;

    @JsonProperty("totalMonthlyIncoming")
    private Double totalMonthlyIncoming;

    @JsonProperty("totalMonthlyExpense")
    private Double totalMonthlyExpense;

    @JsonProperty("incomingOverviewMovement")
    private Map<String, Double> incomingOverviewMovement;

    @JsonProperty("expenseOverviewMovement")
    private Map<String, Double> expenseOverviewMovement;


    @JsonProperty("lastMovements")
    private List<Movement> lastMovements;

    @JsonProperty("members")
    private List<User> members;

    public AccountDetails(Account account){
        this.setId(account.getId());
        this.setName(account.getName());
        this.setDescription(account.getDescription());
        this.setIsSharable(account.getIsSharable());
        this.setIsDeletable(account.getIsDeletable());
        this.setStatus(account.getStatus());
        this.setDefaultUsername(account.getDefaultUsername());

    }
    @Builder(builderMethodName = "newBuilderExt")
    public AccountDetails(@NotNull String id, @NotNull String name, String description, Boolean isSharable, Boolean isDeletable, AccountStatusEnum status, String defaultUsername, List<Category> incomingCategoriesAvalaible, List<Category> expenseCategoriesAvalaible, Double totalMonthlyIncoming, Double totalMonthlyExpense, Map<String, Double> incomingOverviewMovement, Map<String, Double> expenseOverviewMovement, List<Movement> lastMovements) {
        super(id, name, description, isSharable, isDeletable, status, defaultUsername);
        this.incomingCategoriesAvalaible = incomingCategoriesAvalaible;
        this.expenseCategoriesAvalaible = expenseCategoriesAvalaible;
        this.totalMonthlyIncoming = totalMonthlyIncoming;
        this.totalMonthlyExpense = totalMonthlyExpense;
        this.incomingOverviewMovement = incomingOverviewMovement;
        this.expenseOverviewMovement = expenseOverviewMovement;
        this.lastMovements = lastMovements;
    }
}
