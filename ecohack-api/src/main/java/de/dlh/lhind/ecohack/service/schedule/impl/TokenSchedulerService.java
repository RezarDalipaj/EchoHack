package de.dlh.lhind.ecohack.service.schedule.impl;

import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.security.token.TokenProvider;
import de.dlh.lhind.ecohack.service.schedule.ITokenSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenSchedulerService implements ITokenSchedulerService {

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 3 * 60 * 1000)
    @Transactional
    public void deleteExpiredTokens(){
        log.info("Executing scheduler");
        tokenRepository.findAll().forEach(tokenEntity -> {
            var tokenIsValid = tokenProvider.tokenIsValid(tokenEntity.getValue());
            //if token is either a valid access or refresh token don't do anything
            if (Boolean.TRUE.equals(tokenIsValid))
                return;
            // else delete the token
            tokenRepository.delete(tokenEntity);
            log.warn("Deleted token with id {} from db", tokenEntity.getId());
        });
    }
}
