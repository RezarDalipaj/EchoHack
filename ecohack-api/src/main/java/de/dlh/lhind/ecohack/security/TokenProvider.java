package de.dlh.lhind.ecohack.security;

import de.dlh.lhind.ecohack.config.JwtProperties;
import de.dlh.lhind.ecohack.config.JwtSecret;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.entity.Token;
import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

    private String generateToken(Authentication authentication, boolean isAccess) throws UnAuthorizedException {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        var role = getRoleFromUser(user);

        if (isAccess)
            return buildAndSaveAccessToken(user, role);
        return buildAndSaveRefreshToken(user, role);
    }

    public String getRoleFromUser(UserDetails userDetails) throws UnAuthorizedException {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(UnAuthorizedException::new);
    }

    public String buildAndSaveAccessToken(UserDetails user, String role) {
        return buildAndSaveToken(user, role, true);
    }
    public String buildAndSaveRefreshToken(UserDetails user, String role){
        return buildAndSaveToken(user, role, false);
    }

    private String buildAndSaveToken(UserDetails user, String role, boolean isAccess){
        var signingKey = getKeyFromBoolean(isAccess);
        var token = Jwts.builder()
                .setHeaderParam("type", TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(getMinutesFromBoolean(isAccess)).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .claim("role", role)
                .claim("preferred_username", user.getUsername())
                .compact();
        saveToken(user.getUsername(), token);
        return token;
    }

    private Integer getMinutesFromBoolean (boolean isAccess) {
        if (!isAccess)
            return jwtProperties.getRefresh();
        return jwtProperties.getAccess();
    }

    private void saveToken(String username, String token) {
        var user = userService.findUserByEmail(username);
        var tokenEntity = new Token();
        tokenEntity.setUser(user);
        tokenEntity.setToken(token);
        tokenRepository.save(tokenEntity);
    }

    public String generateAccessToken(Authentication authentication) throws UnAuthorizedException {
        return generateToken(authentication, true);
    }

    public String generateRefreshToken(Authentication authentication) throws UnAuthorizedException {
        return generateToken(authentication, false);
    }

    public Optional<Jws<Claims>> validateTokenAndGetJws(String token, boolean isAccess) {
        if (tokenDoesNotExist(token))
            return Optional.empty();
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getKeyFromBoolean(isAccess))
                    .build()
                    .parseClaimsJws(token);

            return Optional.of(jws);
        } catch (ExpiredJwtException exception) {
            log.error("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            log.error("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        return Optional.empty();
    }

    private boolean tokenDoesNotExist(String token){
        var entity = tokenRepository.findByToken(token);
        return entity.isEmpty();
    }

    private byte[] getKeyFromBoolean(boolean isAccess){
        if (isAccess)
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

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "order-api";
    public static final String TOKEN_AUDIENCE = "order-app";
}

