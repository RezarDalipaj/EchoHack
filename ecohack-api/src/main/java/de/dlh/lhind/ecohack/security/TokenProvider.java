package de.dlh.lhind.ecohack.security;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    public String generate(Authentication authentication, Integer minutes) {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        var roles = getRolesFromUser(user);

        return buildToken(user, roles, minutes);
    }

    public String getRolesFromUser(UserDetails userDetails){
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow();
    }

    public String buildToken(UserDetails user, String roles, Integer minutes){
        byte[] signingKey = jwtSecret.getBytes();
        return Jwts.builder()
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
    }

    public Optional<Jws<Claims>> validateTokenAndGetJws(String token) {
        try {
            byte[] signingKey = jwtSecret.getBytes();
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
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

    private Jws<Claims> getClaimsFromToken(String token) throws UnAuthorizedException {
        var claims = validateTokenAndGetJws(token);
        if (claims.isEmpty())
            throw new UnAuthorizedException("Unauthorized!");
        return claims.get();
    }

    public <T> T getClaimFromToken(String token, String claimType, Class<T> claimClass) throws UnAuthorizedException {
        var claims = getClaimsFromToken(token);
        return claims.getBody().get(claimType, claimClass);
    }

    public String getUsernameFromToken(String token) throws UnAuthorizedException {
        var claims = getClaimsFromToken(token);
        return claims.getBody().getSubject();
    }

    public Date getExpirationDateFromToken(String token) throws UnAuthorizedException {
        var claims = getClaimsFromToken(token);
        return claims.getBody().getExpiration();
    }

    public String getRoleFromToken(String token) throws UnAuthorizedException {
        return getClaimFromToken(token, "role", String.class);
    }

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "order-api";
    public static final String TOKEN_AUDIENCE = "order-app";
}

