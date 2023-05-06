package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.mapper.UserMapper;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.ClientRepository;
import de.dlh.lhind.ecohack.repository.UserRepository;
import de.dlh.lhind.ecohack.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder bcryptEncoder;

    @Override
    public UserDto findUserDtoByEmail(String email) {
        var userEntity = findUserByEmail(email);
        return userMapper.userToDto(userEntity);
    }

    @Override
    public User findUserByEmail(String email) {
        var entity = userRepository.findByEmail(email);
        if (entity == null)
            throw new NullPointerException("User with email " + email);
        return entity;
    }

    @Override
    public ClientDto save(ClientDto clientDto) {
        var client = userMapper.dtoToClient(clientDto);
        var user = client.getUser();
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setRole(Role.CLIENT);
        userRepository.save(user);
        client.setUser(user);
        clientRepository.save(client);
        return userMapper.clientToDto(client);
    }
}
