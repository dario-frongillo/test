package it.italiancoders.mybudget.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@AllArgsConstructor
public class Movement {

    public Movement(){}

    @JsonProperty("id")
    //ReadOnly
    private String id;

    @JsonProperty("type")
    @NonNull
    private MovementType type;

    @JsonProperty("amount")
    @NonNull
    private Double amount;

    @JsonProperty("executedBy")
    private User executedBy;

    @JsonProperty("executedAt")
    private Long executedAt;

    @JsonProperty("uptadedAt")
    private Long uptadedAt;

    @JsonProperty("note")
    private String note;

    @JsonProperty("category")
    private Category category;

    @JsonIgnore
    private Account account;


}
