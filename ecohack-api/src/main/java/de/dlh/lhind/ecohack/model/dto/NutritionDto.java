package de.dlh.lhind.ecohack.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NutritionDto {
    private Long id;
    private String name;
    private Double amount;
    private Long mealId;
}
