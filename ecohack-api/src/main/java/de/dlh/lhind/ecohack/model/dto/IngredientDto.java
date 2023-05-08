package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientDto {
    private Long id;
    @NotNull
    private Integer points;
    @NotBlank
    private String name;
}
