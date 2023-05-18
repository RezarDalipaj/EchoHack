package de.dlh.lhind.ecohack.service.business;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.enumeration.Role;

public interface IUserService {
    User findUserByEmail(String email);
    User save(User user, Role role);
    void validateUsername(String username) throws BadRequestException;

    UserDto getUserByUsername(String username);
}
