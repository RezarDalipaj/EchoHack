package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.util.mapper.MappingUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        uses = MappingUtil.class)
public interface UserMapper {

    @Mapping(source = "authorities", target = "role")
    UserDto userDetailsToUserDto(UserDetails userDetails) throws UnAuthorizedException;

    @Mapping(source = "email", target = "username")
    UserDto userToUserDto(User user);

    @Mapping(target = "email", source = "username")
    User userDtoToEntity(UserDto userDto);
}
