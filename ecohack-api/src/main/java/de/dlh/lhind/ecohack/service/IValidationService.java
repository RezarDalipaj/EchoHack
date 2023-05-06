package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;

public interface IValidationService {
    void validateUsername(String username) throws BadRequestException;
}
