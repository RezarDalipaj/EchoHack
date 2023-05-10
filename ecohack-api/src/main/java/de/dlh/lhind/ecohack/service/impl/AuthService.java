package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.security.TokenProvider;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.service.IJwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final IJwtUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    public TokenDto login(LoginDto loginDto) throws UnAuthorizedException {
        String accessToken = authenticateAndGetAccessToken(loginDto);
        String refreshToken = authenticateAndGetRefreshToken(loginDto);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenDto refreshToken(String refreshToken) throws UnAuthorizedException {
        var username = tokenProvider.getUsernameFromRefreshToken(refreshToken);
        var user = userDetailsService.loadUserByUsername(username);
        var roles = tokenProvider.getRoleFromUser(user);
        String accessToken = tokenProvider.buildAndSaveAccessToken(user, roles);
        String newRefreshToken = tokenProvider.buildAndSaveRefreshToken(user, roles);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private String authenticateAndGetAccessToken(LoginDto loginDto) throws UnAuthorizedException {
        return tokenProvider.generateAccessToken(getAuthenticationFromLogin(loginDto));
    }

    private String authenticateAndGetRefreshToken(LoginDto loginDto) throws UnAuthorizedException {
        return tokenProvider.generateRefreshToken(getAuthenticationFromLogin(loginDto));
    }

    private Authentication getAuthenticationFromLogin(LoginDto loginDto){
        return authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
    }
}
