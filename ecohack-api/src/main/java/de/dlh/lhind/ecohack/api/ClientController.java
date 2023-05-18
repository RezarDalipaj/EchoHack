package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.response.QuizResponse;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.security.token.TokenProvider;
import de.dlh.lhind.ecohack.service.business.IClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;
    private final TokenProvider tokenProvider;

    @PostMapping("/auth/client/signup")
    public ResponseEntity<TokenDto> saveClient(@Valid @RequestBody ClientDto clientDto) throws BadRequestException, UnAuthorizedException {
        return ResponseEntity.ok(clientService.save(clientDto));
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/take/quiz")
    public ResponseEntity<QuizResponse> takeQuiz(@Valid @RequestBody QuestionnaireDto questionnaireDto, HttpServletRequest request) throws UnAuthorizedException, BadRequestException {
        return ResponseEntity.ok(clientService.takeQuiz(questionnaireDto, tokenProvider.getUsernameFromRequest(request)));
    }
}
