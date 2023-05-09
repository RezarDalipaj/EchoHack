package de.dlh.lhind.ecohack.util;

import de.dlh.lhind.ecohack.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@AllArgsConstructor
public class TokenUtil {
    private final TokenProvider tokenProvider;
    public String usernameFromToken(HttpServletRequest request) {
        var token = getTokenFromRequest(request);
        return tokenProvider.getUsernameFromToken(token, true);
    }

    public String getTokenFromRequest(HttpServletRequest request){
        return getJwtFromRequest(request).orElseThrow();
    }

    public Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)) {
            return Optional.of(tokenHeader.replace(TOKEN_PREFIX, ""));
        }
        return Optional.empty();
    }

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
