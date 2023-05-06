package de.dlh.lhind.ecohack.util;

import de.dlh.lhind.ecohack.security.config.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenUtil {

    private final JwtTokenUtil jwtTokenUtil;

    public String usernameFromToken(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
