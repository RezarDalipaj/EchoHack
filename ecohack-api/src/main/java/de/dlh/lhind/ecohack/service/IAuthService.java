package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;

public interface IAuthService {
    TokenDto login(LoginDto loginDto);
    TokenDto refreshToken(String username);
}
