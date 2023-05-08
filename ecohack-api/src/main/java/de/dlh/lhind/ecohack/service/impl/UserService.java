package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.UserRepository;
import de.dlh.lhind.ecohack.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    @Override
    public User findUserByEmail(String email) {
        var entity = userRepository.findByEmail(email);
        if (entity == null)
            throw new NullPointerException("User with email " + email);
        return entity;
    }

    @Override
    public User save(User user, Role role){
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

}
