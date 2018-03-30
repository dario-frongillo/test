package it.italiancoders.mybudget.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private String id;

    @NotNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("description")
    private String description;

    @JsonProperty("isSharable")
    private Boolean isSharable;

    @JsonProperty("isDeletable")
    private Boolean isDeletable;

    @JsonProperty("status")
    private AccountStatusEnum status;


    @JsonIgnore
    @JsonProperty("defaultUsername")
    private String defaultUsername;

    public Account(){}

}
