package it.italiancoders.mybudget.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@AllArgsConstructor
public class Movement {

    public Movement(){}

    private String id;

    private MovementType movementType;

    private Double amount;

    private User executedBy;

    private Long executedAt;

    private Long uptadedAt;

    private String note;

    private Category category;


}
