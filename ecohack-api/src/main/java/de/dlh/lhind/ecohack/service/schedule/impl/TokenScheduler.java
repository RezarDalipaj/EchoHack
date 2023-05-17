package de.dlh.lhind.ecohack.service.schedule.impl;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.security.TokenProvider;
import de.dlh.lhind.ecohack.service.schedule.ITokenScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenScheduler implements ITokenScheduler {

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Scheduled(initialDelay = 60000, fixedDelay = 180000)
    @Transactional
    public void deleteExpiredTokens(){
        log.info("Executing scheduler");
        tokenRepository.findAll().forEach(token -> {
            // if it's a valid access token don't do anything
            try {
                tokenProvider.getUsernameFromAccessToken(token.getValue());
            } catch (UnAuthorizedException unAuthorizedException){
                // if it's a valid refresh token don't do anything
                try {
                    tokenProvider.getUsernameFromRefreshToken(token.getValue());
                } catch (UnAuthorizedException exception) {
                    // else delete the token
                    tokenRepository.delete(token);
                    log.warn("Deleted token with id {} from db", token.getId());
                }
            }
        });
    }
}
