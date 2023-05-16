package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.security.TokenProvider;
import de.dlh.lhind.ecohack.service.ILogoutService;
import de.dlh.lhind.ecohack.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements ILogoutService {

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public void logout(HttpServletRequest request
            , HttpServletResponse response, Authentication authentication) {
        var token = tokenProvider.getTokenFromRequest(request);
        // validating if request has an access token
        try {
            tokenProvider.getUsernameFromRequest(request);
        } catch (UnAuthorizedException ignored){
            log.error(Constants.UNAUTHORIZED_MESSAGE);
            return;
        }
        var storedToken = tokenRepository.findByValue(token).orElseThrow();
        tokenRepository.deleteById(storedToken.getId() + 1);
        tokenRepository.delete(storedToken);
    }
}
