package de.dlh.lhind.ecohack.util;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final TokenProvider tokenProvider;
    public String usernameFromToken(HttpServletRequest request) throws UnAuthorizedException {
        var token = getTokenFromRequest(request);
        return tokenProvider.getUsernameFromAccessToken(token);
    }

    public String getTokenFromRequest(HttpServletRequest request){
        return getJwtFromRequest(request).orElseThrow();
    }

    public Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader(Constants.Token.TOKEN_HEADER);
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(Constants.Token.TOKEN_PREFIX)) {
            return Optional.of(tokenHeader.replace(Constants.Token.TOKEN_PREFIX, ""));
        }
        return Optional.empty();
    }
}
