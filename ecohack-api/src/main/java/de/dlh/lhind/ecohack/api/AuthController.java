package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.LogoutDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.service.security.IAuthService;
import de.dlh.lhind.ecohack.service.security.ILogoutService;
import de.dlh.lhind.ecohack.util.constants.Constants;
import de.dlh.lhind.ecohack.util.security.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final ILogoutService logoutService;

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginRequest) throws UnAuthorizedException {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping(Constants.REFRESH_PATH)
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest request) throws UnAuthorizedException {
        return ResponseEntity.ok(authService.refreshToken(TokenUtil.getTokenFromRequest(request)));
    }

    @GetMapping("/success/logout")
    public ResponseEntity<LogoutDto> logoutSuccess() throws UnAuthorizedException {
        return ResponseEntity.ok(logoutService.successLogout());
    }
}
