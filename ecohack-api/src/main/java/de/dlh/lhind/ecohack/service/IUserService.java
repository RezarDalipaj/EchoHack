package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.User;

import java.util.List;

public interface IUserService {
    UserDto findUserDtoByEmail(String email);
    User findUserByEmail(String email);
    ClientDto save(ClientDto clientDto);
    List<User> findAll();
}
