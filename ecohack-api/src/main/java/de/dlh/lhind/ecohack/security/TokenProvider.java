package de.dlh.lhind.ecohack.security;

import de.dlh.lhind.ecohack.config.JwtProperties;
import de.dlh.lhind.ecohack.config.JwtSecret;
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

    public boolean tokenDoesNotExist(String token){
        var entity = tokenRepository.findByToken(token);
        return entity.isEmpty();
    }

    public String generate(Authentication authentication, Integer minutes) {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        var role = getRoleFromUser(user);

        return buildAndSaveToken(user, role, minutes);
    }

    private void saveToken(String username, String token) {
        var user = userService.findUserByEmail(username);
        var tokenEntity = new Token();
        tokenEntity.setUser(user);
        tokenEntity.setToken(token);
        tokenRepository.save(tokenEntity);
    }

    public String getRoleFromUser(UserDetails userDetails){
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow();
    }

    public String buildAndSaveToken(UserDetails user, String roles, Integer minutes){
        var signingKey = getSigningKeyFromMinutes(minutes);
        var token = Jwts.builder()
                .setHeaderParam("type", TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(minutes).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .claim("role", roles)
                .claim("preferred_username", user.getUsername())
                .compact();
        saveToken(user.getUsername(), token);
        return token;
    }

    private byte[] getSigningKeyFromMinutes(Integer minutes) {
        if (minutes.equals(jwtProperties.getRefresh()))
            return jwtSecret.getRefresh().getBytes();
        return jwtSecret.getAccess().getBytes();
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

    public byte[] getKeyFromBoolean(boolean isAccess){
        if (isAccess)
            return jwtSecret.getAccess().getBytes();
        return jwtSecret.getRefresh().getBytes();
    }

    private Jws<Claims> getClaimsFromToken(String token, boolean isAccess) {
        var claims = validateTokenAndGetJws(token, isAccess);
        return claims.orElseThrow();
    }

    public <T> T getClaimFromToken(String token, String claimType, Class<T> claimClass) {
        var claims = getClaimsFromToken(token, true);
        return claims.getBody().get(claimType, claimClass);
    }

    public String getUsernameFromToken(String token, boolean isAccess) {
        var claims = getClaimsFromToken(token, isAccess);
        return claims.getBody().getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        var claims = getClaimsFromToken(token, true);
        return claims.getBody().getExpiration();
    }

    public void expireToken(String token){
        var claims = getClaimsFromToken(token, true);
        claims.getBody().setExpiration(new Date());
    }

    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, "role", String.class);
    }

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "order-api";
    public static final String TOKEN_AUDIENCE = "order-app";
}

