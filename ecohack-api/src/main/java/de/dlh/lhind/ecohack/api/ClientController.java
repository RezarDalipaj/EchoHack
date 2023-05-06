package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.service.IClientService;
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
public class ClientController {

    private final IClientService clientService;
    private final IAuthService authService;

    @PostMapping("/auth/client/signup")
    public ResponseEntity<TokenDto> saveClient(@RequestBody ClientDto clientDto) throws BadRequestException {
        return ResponseEntity.ok(clientService.save(clientDto));
    }

    @PostMapping("/auth/login")
    public TokenDto login(@Valid @RequestBody LoginDto loginRequest) {
        return authService.login(loginRequest);
    }
}
