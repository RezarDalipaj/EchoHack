package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.service.ILogoutService;
import de.dlh.lhind.ecohack.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements ILogoutService {

    private final TokenRepository tokenRepository;
    private final TokenUtil tokenUtil;

    @Override
    public void logout(HttpServletRequest request
            , HttpServletResponse response, Authentication authentication) {
        var token = tokenUtil.getTokenFromRequest(request);
        var storedToken = tokenRepository.findByToken(token).orElseThrow();
        var loggedOutUser = storedToken.getUser();
        var tokensOfUser = tokenRepository.findAllByUser(loggedOutUser);
        tokensOfUser.forEach(tokenToBeRevoked ->
                tokenToBeRevoked.setRevoked(true));
        tokenRepository.saveAll(tokensOfUser);
    }
}
