package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.ProviderDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.service.IProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProviderController {

    private final IProviderService providerService;

    @PostMapping("/auth/provider/signup")
    public ResponseEntity<TokenDto> saveProvider(@RequestBody ProviderDto providerDto) throws BadRequestException {
        return ResponseEntity.ok(providerService.saveProvider(providerDto));
    }
}
