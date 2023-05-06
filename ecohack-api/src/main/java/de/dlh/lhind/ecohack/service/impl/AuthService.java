package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.security.config.JwtTokenUtil;
import de.dlh.lhind.ecohack.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenProvider;
    private final JwtUserDetailsService userDetailsService;

    @Override
    public TokenDto login(LoginDto loginDto) {
        String token = authenticateAndGetToken(loginDto.getUsername(), loginDto.getPassword());
        return TokenDto.builder()
                .jwtToken(token)
                .build();
    }

    private String authenticateAndGetToken(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        return tokenProvider.generateToken(userDetails);
    }
}
