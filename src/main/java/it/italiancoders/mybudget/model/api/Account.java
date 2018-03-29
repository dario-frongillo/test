package it.italiancoders.mybudget.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@AllArgsConstructor
public class Account {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    private Boolean isSharable;

    private Boolean isDeletable;

    private AccountStatusEnum status;


    @JsonIgnore
    private String defaultUsername;

    public Account(){}

}
