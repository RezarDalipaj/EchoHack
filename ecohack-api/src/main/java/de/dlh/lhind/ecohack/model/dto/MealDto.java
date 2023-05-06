package de.dlh.lhind.ecohack.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MealDto {
    private Long id;
    private String name;
    private List<NutritionDto> nutritions;
    private List<TagDto> tags;
    private List<IngredientDto> ingredients;
}
