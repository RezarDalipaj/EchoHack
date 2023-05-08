package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final TokenUtil tokenUtil;

    @PostMapping("/auth/login")
    public TokenDto login(@Valid @RequestBody LoginDto loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/refresh/token")
    public TokenDto refreshToken(HttpServletRequest request) throws UnAuthorizedException {
        return authService.refreshToken(tokenUtil.usernameFromToken(request));
    }
}
