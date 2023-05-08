package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMealDto {
    @NotBlank
    private Long mealId;
}
