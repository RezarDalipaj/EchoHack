package de.dlh.lhind.ecohack.service.schedule;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.repository.TokenRepository;
import de.dlh.lhind.ecohack.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenScheduler {

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    @Scheduled(initialDelay = 120000, fixedDelay = 300000)
    public void deleteExpiredTokens(){
        tokenRepository.findAll().forEach(token -> {
            try {
                tokenProvider.getExpirationDateFromToken(token.getToken());
            } catch (UnAuthorizedException unAuthorizedException){
                tokenRepository.delete(token);
            }
        });
    }
}
