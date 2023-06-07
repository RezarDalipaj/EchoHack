package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.model.dto.OrderMealDto;
import de.dlh.lhind.ecohack.model.entity.Meal;
import de.dlh.lhind.ecohack.model.entity.Order;
import de.dlh.lhind.ecohack.util.mapper.MappingUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        uses = MappingUtil.class)
public interface OrderMapper {
    @Mapping(source = "orderDetail.comment", target = "comment")
    @Mapping(source = "client.user.email", target = "username")
    @Mapping(source = "orderDetail.price", target = "amountPayed")
    OrderDto toOrderDto(Order order);

    @Mapping(source = "id", target = "mealId")
    OrderMealDto toOrderMealDto(Meal meal);
}
