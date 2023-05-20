package de.dlh.lhind.ecohack.security.token;

import de.dlh.lhind.ecohack.config.properties.JwtProperties;
import de.dlh.lhind.ecohack.config.properties.JwtSecret;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.mapper.UserMapper;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import de.dlh.lhind.ecohack.model.entity.Token;
import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.util.constants.Constants;
import de.dlh.lhind.ecohack.util.security.TokenUtil;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final JwtSecret jwtSecret;
    private final JwtProperties jwtProperties;

    @Transactional
    public String generateAccessToken(Authentication authentication) throws UnAuthorizedException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        var userDto = userMapper.userDetailsToUserDto(userDetails);
        return buildAndSaveAccessToken(userDto);
    }

    @Transactional
    public String buildAndSaveAccessToken(UserDto user) {
        return buildAndSaveToken(user, true);
    }
    @Transactional
    public String buildAndSaveRefreshToken(UserDto user) {
        return buildAndSaveToken(user, false);
    }

    private String buildAndSaveToken(UserDto userDto, boolean isAccessToken) {
        var signingKey = getKeyFromBoolean(isAccessToken);
        var token = Jwts.builder()
                .setHeaderParam("type", Constants.Token.TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(getMinutesFromBoolean(isAccessToken)).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(Constants.Token.TOKEN_ISSUER)
                .setAudience(Constants.Token.TOKEN_AUDIENCE)
                .setSubject(userDto.getUsername())
                .claim("role", userDto.getRole())
                .compact();
        saveToken(token);
        return token;
    }

    private Integer getMinutesFromBoolean (boolean isAccess) {
        if (isAccess)
            return jwtProperties.getAccess();
        return jwtProperties.getRefresh();
    }

    private void saveToken(String token) {
        var tokenEntity = Token.builder()
                .value(token)
                .build();
        tokenRepository.save(tokenEntity);
    }

    private Optional<Jws<Claims>> validateAccessTokenAndGetJws(String token){
        return validateTokenAndGetJws(token, true);
    }

    private Optional<Jws<Claims>> validateRefreshTokenAndGetJws(String token){
        return validateTokenAndGetJws(token, false);
    }

    public boolean accessTokenIsValid(String token){
        return validateAccessTokenAndGetJws(token).isPresent();
    }

    public boolean refreshTokenIsValid(String token){
        return validateRefreshTokenAndGetJws(token).isPresent();
    }

    public Boolean tokenIsValid(String token){
        return accessTokenIsValid(token) || refreshTokenIsValid(token);
    }

    public Optional<Jws<Claims>> validateTokenAndGetJws(String token, boolean isAccessToken) {
        if (tokenDoesNotExist(token))
            return Optional.empty();
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getKeyFromBoolean(isAccessToken))
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
        return !tokenRepository.existsByValue(token);
    }

    private byte[] getKeyFromBoolean(boolean isAccess){
        if (isAccess)
            return jwtSecret.getAccess().getBytes();
        return jwtSecret.getRefresh().getBytes();
    }

    private Jws<Claims> getClaimsFromRefreshToken(String token) throws UnAuthorizedException {
        var claims = validateRefreshTokenAndGetJws(token);
        return claims.orElseThrow(UnAuthorizedException::new);
    }

    private Jws<Claims> getClaimsFromAccessToken(String token) throws UnAuthorizedException {
        var claims = validateAccessTokenAndGetJws(token);
        return claims.orElseThrow(UnAuthorizedException::new);
    }

    public  <T> T getClaimFromAccessToken(String token, String claimType, Class<T> claimClass) throws UnAuthorizedException {
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
        return getClaimFromAccessToken(token, "role", String.class);
    }
}

