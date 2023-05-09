package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.security.TokenProvider;
import de.dlh.lhind.ecohack.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TokenController {
    private final TokenProvider tokenProvider;
    private final TokenUtil tokenUtil;

    @GetMapping("/token/date")
    public ResponseEntity<Date> getDateFromToken(HttpServletRequest request) throws UnAuthorizedException {
        return ResponseEntity.ok(tokenProvider.getExpirationDateFromToken(tokenUtil.getTokenFromRequest(request)));
    }

    @GetMapping("/token/role")
    public ResponseEntity<String> getRoleFromToken(HttpServletRequest request) throws UnAuthorizedException {
        return ResponseEntity.ok(tokenProvider.getRoleFromToken(tokenUtil.getTokenFromRequest(request)));
    }
}
