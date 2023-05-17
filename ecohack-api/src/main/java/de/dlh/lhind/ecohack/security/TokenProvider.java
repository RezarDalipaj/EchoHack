package de.dlh.lhind.ecohack.security;

import de.dlh.lhind.ecohack.config.JwtProperties;
import de.dlh.lhind.ecohack.config.JwtSecret;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.entity.Token;
import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.service.IUserService;
import de.dlh.lhind.ecohack.util.Constants;
import de.dlh.lhind.ecohack.util.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class TokenProvider {

    private final TokenRepository tokenRepository;
    private final IUserService userService;
    private final JwtSecret jwtSecret;
    private final JwtProperties jwtProperties;

    public TokenProvider(TokenRepository tokenRepository, @Lazy IUserService userService, JwtSecret jwtSecret, JwtProperties jwtProperties) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.jwtSecret = jwtSecret;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public String generateAccessToken(Authentication authentication) throws UnAuthorizedException {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        return buildAndSaveAccessToken(user);
    }

    @Transactional
    public String buildAndSaveAccessToken(UserDetails user) throws UnAuthorizedException {
        return buildAndSaveToken(user, true);
    }
    @Transactional
    public String buildAndSaveRefreshToken(UserDetails user) throws UnAuthorizedException {
        return buildAndSaveToken(user, false);
    }

    @Transactional
    public String buildAndSaveToken(UserDetails user, Boolean isAccess) throws UnAuthorizedException {
        var signingKey = getKeyFromBoolean(isAccess);
        var token = Jwts.builder()
                .setHeaderParam("type", Constants.Token.TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(getMinutesFromBoolean(isAccess)).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(Constants.Token.TOKEN_ISSUER)
                .setAudience(Constants.Token.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .claim("role", getRoleFromUser(user))
                .claim("preferred_username", user.getUsername())
                .compact();
        saveToken(user.getUsername(), token);
        return token;
    }

    private String getRoleFromUser(UserDetails userDetails) throws UnAuthorizedException {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(UnAuthorizedException::new);
    }

    private Integer getMinutesFromBoolean (Boolean isAccess) {
        if (Boolean.TRUE.equals(isAccess))
            return jwtProperties.getAccess();
        return jwtProperties.getRefresh();
    }

    @Transactional
    public void saveToken(String username, String token) {
        var user = userService.findUserByEmail(username);
        var tokenEntity = Token.builder()
                .user(user)
                .value(token)
                .build();
        tokenRepository.save(tokenEntity);
    }

    public Optional<Jws<Claims>> validateTokenAndGetJws(String token, Boolean isAccess) {
        if (tokenDoesNotExist(token))
            return Optional.empty();
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getKeyFromBoolean(isAccess))
                    .build()
                    .parseClaimsJws(token);

            return Optional.of(jws);
        } catch (ExpiredJwtException exception) {
            log.error("Request to parse expired JWT failed : {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("Request to parse unsupported JWT failed : {}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Request to parse invalid JWT failed : {}", exception.getMessage());
        } catch (SignatureException exception) {
            log.error("Request to parse JWT with invalid signature failed : {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("Request to parse empty or null JWT failed : {}", exception.getMessage());
        }
        return Optional.empty();
    }

    private boolean tokenDoesNotExist(String token){
        var entity = tokenRepository.findByValue(token);
        return entity.isEmpty();
    }

    private byte[] getKeyFromBoolean(Boolean isAccess){
        if (Boolean.TRUE.equals(isAccess))
            return jwtSecret.getAccess().getBytes();
        return jwtSecret.getRefresh().getBytes();
    }

    private Jws<Claims> getClaimsFromRefreshToken(String token) throws UnAuthorizedException {
        var claims = validateTokenAndGetJws(token, false);
        return claims.orElseThrow(UnAuthorizedException::new);
    }

    private Jws<Claims> getClaimsFromAccessToken(String token) throws UnAuthorizedException {
        var claims = validateTokenAndGetJws(token, true);
        return claims.orElseThrow(UnAuthorizedException::new);
    }

    public  <T> T getClaimFromToken(String token, String claimType, Class<T> claimClass) throws UnAuthorizedException {
        var claims = getClaimsFromAccessToken(token);
        return claims.getBody().get(claimType, claimClass);
    }

    public String getUsernameFromRequest(HttpServletRequest request) throws UnAuthorizedException {
        var token = TokenUtil.getTokenFromRequest(request);
        return getUsernameFromAccessToken(token);
    }

    public String getUsernameFromAccessToken(String token) throws UnAuthorizedException {
        var claims = getClaimsFromAccessToken(token);
        return claims.getBody().getSubject();
    }

    public String getUsernameFromRefreshToken(String token) throws UnAuthorizedException {
        var claims = getClaimsFromRefreshToken(token);
        return claims.getBody().getSubject();
    }

    public Date getExpirationDateFromToken(String token) throws UnAuthorizedException {
        var claims = getClaimsFromAccessToken(token);
        return claims.getBody().getExpiration();
    }

    public void expireToken(String token) throws UnAuthorizedException {
        var claims = getClaimsFromAccessToken(token);
        claims.getBody().setExpiration(new Date());
    }

    public String getRoleFromToken(String token) throws UnAuthorizedException {
        return getClaimFromToken(token, "role", String.class);
    }
}

