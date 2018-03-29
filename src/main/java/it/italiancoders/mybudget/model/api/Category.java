package it.italiancoders.mybudget.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@AllArgsConstructor
public class Category {


    private MovementType type;

    private String id;

    private String value;

    private Boolean isEditable;

    public Category(){}

}
