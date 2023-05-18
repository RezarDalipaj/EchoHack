package de.dlh.lhind.ecohack.service.business.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.mapper.UserMapper;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.UserRepository;
import de.dlh.lhind.ecohack.service.business.IUserService;
import de.dlh.lhind.ecohack.util.security.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;
    private final UserMapper userMapper;

    private User findUserByEmail(String email) {
        var entity = userRepository.findByEmail(email);
        if (entity == null)
            throw new NullPointerException("User with email " + email + " does not exist");
        return entity;
    }

    @Override
    @Transactional
    public User save(User user, Role role){
        var saltedPassword = PasswordUtil.getSaltedPassword(user.getPassword());
        user.setPassword(bcryptEncoder.encode(saltedPassword));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public void validateUsername(String username) throws BadRequestException {
        if (userRepository.existsByEmail(username))
            throw new BadRequestException("Username already exists");
    }

    @Override
    public UserDto getUserByUsername(String username){
        var user = findUserByEmail(username);
        return userMapper.userToUserDto(user);
    }

}
