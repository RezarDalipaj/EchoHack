package de.dlh.lhind.ecohack.model.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MealDto {
    private Long id;
    @NotBlank
    private String name;
    @Lob
    private String image;
    @NotNull
    private Double price;
    private List<NutritionDto> nutritions;
    private List<TagDto> tags;
    private List<IngredientDto> ingredients;
}
