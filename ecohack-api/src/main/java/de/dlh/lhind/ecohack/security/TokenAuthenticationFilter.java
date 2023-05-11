package de.dlh.lhind.ecohack.security;

import de.dlh.lhind.ecohack.service.IJwtUserDetailsService;
import de.dlh.lhind.ecohack.util.Constants;
import de.dlh.lhind.ecohack.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            tokenUtil.getJwtFromRequest(request)
                    .flatMap(token -> tokenProvider.validateTokenAndGetJws(token,isNotRefreshEndpoint(request)))
                    .ifPresent(jws -> {
                        String username = jws.getBody().getSubject();
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        var authentication = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        } catch (Exception e) {
            log.error("Cannot set user authentication", e);
        }
        chain.doFilter(request, response);
    }

    private Boolean isNotRefreshEndpoint(HttpServletRequest request) {
        var endpoint = request.getRequestURL().toString();
        return !endpoint.contains(Constants.REFRESH_PATH);
    }
}
