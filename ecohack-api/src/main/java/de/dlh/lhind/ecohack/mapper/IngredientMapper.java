package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.dto.IngredientDto;
import de.dlh.lhind.ecohack.model.entity.Ingredient;
import de.dlh.lhind.ecohack.util.mapper.MappingUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        uses = MappingUtil.class)
public interface IngredientMapper {

    IngredientDto toDto(Ingredient ingredient);
    Ingredient toIngredient(IngredientDto ingredientDto);

    List<IngredientDto> toDtoList(List<Ingredient> ingredient);
    List<Ingredient> toIngredientList(List<IngredientDto> ingredientDto);
}
