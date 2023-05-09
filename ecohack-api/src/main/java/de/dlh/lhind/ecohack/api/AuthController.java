package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.request.RefreshDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginRequest) throws UnAuthorizedException {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/auth/refresh/token")
    public ResponseEntity<TokenDto> refreshToken(@Valid @RequestBody RefreshDto refreshDto) throws UnAuthorizedException {
        return ResponseEntity.ok(authService.refreshToken(refreshDto));
    }
}
