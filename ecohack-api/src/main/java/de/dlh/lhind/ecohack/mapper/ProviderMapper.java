package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.dto.ProviderDto;
import de.dlh.lhind.ecohack.model.entity.FoodProvider;
import de.dlh.lhind.ecohack.util.mapper.MappingUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        uses = MappingUtil.class)
public interface ProviderMapper {

    @Mapping(source = "user.email", target = "username")
    @Mapping(source = "user.password", target = "password")
    ProviderDto toProviderDto(FoodProvider provider);

    @Mapping(target = "user.email", source = "username")
    @Mapping(target = "user.password", source = "password")
    FoodProvider toProvider(ProviderDto providerDto);
}
