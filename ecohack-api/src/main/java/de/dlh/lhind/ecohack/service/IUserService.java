package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.enumeration.Role;

public interface IUserService {
    User findUserByEmail(String email);
    User save(User user, Role role);
}
