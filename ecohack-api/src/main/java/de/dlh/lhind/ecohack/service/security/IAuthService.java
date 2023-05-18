package de.dlh.lhind.ecohack.service.security;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;

public interface IAuthService {
    TokenDto login(LoginDto loginDto) throws UnAuthorizedException;
    TokenDto refreshToken(String refreshToken) throws UnAuthorizedException;
}
