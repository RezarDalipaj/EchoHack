package de.dlh.lhind.ecohack.service.security.impl;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.response.LogoutDto;
import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.security.token.TokenProvider;
import de.dlh.lhind.ecohack.service.security.ILogoutService;
import de.dlh.lhind.ecohack.util.constants.Constants;
import de.dlh.lhind.ecohack.util.security.TokenUtil;
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
    private boolean isLoggedOut = false;

    @Override
    @Transactional
    public void logout(HttpServletRequest request
            , HttpServletResponse response, Authentication authentication) {
        var token = TokenUtil.getTokenFromRequest(request);
        // validating if request has an access token
        if (!tokenProvider.accessTokenIsValid(token)){
            log.error(Constants.UNAUTHORIZED_MESSAGE);
            isLoggedOut = false;
            return;
        }
        var storedToken = tokenRepository.findByValue(token).orElseThrow();
        tokenRepository.deleteById(storedToken.getId() + 1);
        tokenRepository.delete(storedToken);
        isLoggedOut = true;
    }

    @Override
    public LogoutDto successLogout() throws UnAuthorizedException {
        if (isLoggedOut)
            return LogoutDto.builder()
                .logoutMessage(Constants.LOGOUT_SUCCESS)
                .build();
        throw new UnAuthorizedException("Could not logout! Invalid token!");
    }
}
