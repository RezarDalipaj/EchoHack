package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.model.entity.Order;
import de.dlh.lhind.ecohack.util.mapper.MappingUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        uses = MappingUtil.class)
public interface OrderMapper {
    OrderDto toOrderDto(Order order);
}
