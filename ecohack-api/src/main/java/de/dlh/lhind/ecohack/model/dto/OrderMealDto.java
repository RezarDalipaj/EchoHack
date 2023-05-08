package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMealDto {
    @NotNull
    private Long mealId;
}
