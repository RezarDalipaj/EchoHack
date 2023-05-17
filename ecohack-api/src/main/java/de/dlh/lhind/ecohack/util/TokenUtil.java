package de.dlh.lhind.ecohack.util;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Optional;

@UtilityClass
public final class TokenUtil {

    public static String getTokenFromRequest(HttpServletRequest request){
        return getJwtFromRequest(request).orElseThrow();
    }

    public static Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader(Constants.Token.TOKEN_HEADER);
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(Constants.Token.TOKEN_PREFIX)) {
            return Optional.of(tokenHeader.replace(Constants.Token.TOKEN_PREFIX, ""));
        }
        return Optional.empty();
    }

    public static String getRoleFromUser(UserDetails userDetails) throws UnAuthorizedException {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(UnAuthorizedException::new);
    }
}
