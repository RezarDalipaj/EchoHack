package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.entity.Client;
import de.dlh.lhind.ecohack.util.mapper.MappingUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        uses = MappingUtil.class)
public interface ClientMapper {
    @Mapping(source = "user.email", target = "username")
    @Mapping(source = "user.password", target = "password")
    ClientDto clientToDto(Client client);

    @Mapping(target = "user.email", source = "username")
    @Mapping(target = "user.password", source = "password")
    Client dtoToClient(ClientDto clientDto);
}
