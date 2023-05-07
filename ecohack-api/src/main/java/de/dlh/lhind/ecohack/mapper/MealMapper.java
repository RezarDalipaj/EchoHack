package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.entity.Meal;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface MealMapper {

    MealDto toMealDto(Meal meal);
    List<MealDto> toMealDtoList(List<Meal> meals);
}
