package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.enumeration.Role;

import java.util.List;

public interface IUserService {
    UserDto findUserDtoByEmail(String email);
    User findUserByEmail(String email);
    List<User> findAll();

    User save(User user, Role role);
}
