package de.dlh.lhind.ecohack.model.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MealDto {
    private Long id;
    private String name;
    @Lob
    private String image;
    private Long price;
    private List<NutritionDto> nutritions;
    private List<TagDto> tags;
    private List<IngredientDto> ingredients;
}
