package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.request.RefreshDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.security.TokenProvider;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.service.IJwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final IJwtUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    public TokenDto login(LoginDto loginDto) {
        String accessToken = authenticateAndGetAccessToken(loginDto);
        String refreshToken = authenticateAndGetRefreshToken(loginDto);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenDto refreshToken(RefreshDto refreshDto) {
        var username = tokenProvider.getUsernameFromRefreshToken(refreshDto.getRefreshToken());
        var user = userDetailsService.loadUserByUsername(username);
        var roles = tokenProvider.getRoleFromUser(user);
        String accessToken = tokenProvider.buildAndSaveAccessToken(user, roles);
        String refreshToken = tokenProvider.buildAndSaveRefreshToken(user, roles);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String authenticateAndGetAccessToken(LoginDto loginDto) {
        var authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        return tokenProvider.generateAccessToken(authentication);
    }

    private String authenticateAndGetRefreshToken(LoginDto loginDto) {
        var authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        return tokenProvider.generateRefreshToken(authentication);
    }
}
