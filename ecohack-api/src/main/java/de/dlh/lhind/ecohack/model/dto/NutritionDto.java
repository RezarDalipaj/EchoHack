package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NutritionDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Double amount;
    @NotNull
    private Long mealId;
}
