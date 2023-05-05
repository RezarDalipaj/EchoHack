package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.dto.UserDto;

public interface UserMapper {

    UserDto toUserDto(User user);
}