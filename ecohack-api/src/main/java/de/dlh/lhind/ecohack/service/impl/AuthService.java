package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.config.JwtProperties;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.security.TokenProvider;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.service.IJwtUserDetailsService;
import de.dlh.lhind.ecohack.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final IJwtUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public TokenDto login(LoginDto loginDto) {
        String accessToken = authenticateAndGetToken(loginDto, jwtProperties.getAccess());
        String refreshToken = authenticateAndGetToken(loginDto, jwtProperties.getRefresh());
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenDto refreshToken(String username) {
        var user = userDetailsService.loadUserByUsername(username);
        var roles = tokenProvider.getRolesFromUser(user);
        String accessToken = tokenProvider.buildToken(user, roles, jwtProperties.getAccess());
        String refreshToken = tokenProvider.buildToken(user, roles, jwtProperties.getRefresh());
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String authenticateAndGetToken(LoginDto loginDto, Integer minutes) {
        var authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        return tokenProvider.generate(authentication, minutes);
    }
}
