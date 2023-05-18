package de.dlh.lhind.ecohack.service.business;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.User;

public interface IUserService {
    User saveUser(UserDto user);
    void validateUsername(String username) throws BadRequestException;

    UserDto getUserByUsername(String username);
    UserDto mapEntityToDto(User user);
}
