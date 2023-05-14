package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.UserRepository;
import de.dlh.lhind.ecohack.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    @Override
    public User findUserByEmail(String email) {
        var entity = userRepository.findByEmail(email);
        if (entity == null)
            throw new NullPointerException("User with email " + email + " does not exist");
        return entity;
    }

    @Override
    @Transactional
    public User save(User user, Role role){
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public void validateUsername(String username) throws BadRequestException {
        if (userRepository.existsByEmail(username))
            throw new BadRequestException("Username already exists");
    }

}
