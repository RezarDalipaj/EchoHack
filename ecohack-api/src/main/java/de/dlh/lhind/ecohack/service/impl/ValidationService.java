package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.repository.UserRepository;
import de.dlh.lhind.ecohack.service.IValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService implements IValidationService {

    private final UserRepository userRepository;
    @Override
    public void validateUsername(String username) throws BadRequestException {
        if (userRepository.existsByEmail(username))
            throw new BadRequestException("Username already exists");
    }
}
