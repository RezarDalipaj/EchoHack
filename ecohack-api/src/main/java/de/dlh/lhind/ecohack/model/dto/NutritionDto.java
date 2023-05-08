package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NutritionDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private Double amount;
    @NotBlank
    private Long mealId;
}
