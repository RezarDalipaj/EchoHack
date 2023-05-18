package de.dlh.lhind.ecohack.service.security.impl;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.security.token.TokenProvider;
import de.dlh.lhind.ecohack.service.security.IAuthService;
import de.dlh.lhind.ecohack.service.business.IUserService;
import de.dlh.lhind.ecohack.util.security.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final TokenProvider tokenProvider;

    @Override
    public TokenDto login(LoginDto loginDto) throws UnAuthorizedException {
        String accessToken = authenticateAndGetAccessToken(loginDto);
        var user = userService.getUserByUsername(loginDto.getUsername());
        String refreshToken = tokenProvider.buildAndSaveRefreshToken(user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenDto refreshToken(String refreshToken) throws UnAuthorizedException {
        var username = tokenProvider.getUsernameFromRefreshToken(refreshToken);
        var user = userService.getUserByUsername(username);
        String accessToken = tokenProvider.buildAndSaveAccessToken(user);
        String newRefreshToken = tokenProvider.buildAndSaveRefreshToken(user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private String authenticateAndGetAccessToken(LoginDto loginDto) throws UnAuthorizedException {
        var saltedPassword = PasswordUtil.getSaltedPassword(loginDto.getPassword());
        var authentication = authenticationManager.authenticate
        (new UsernamePasswordAuthenticationToken(loginDto.getUsername(), saltedPassword));
        return tokenProvider.generateAccessToken(authentication);
    }
}
