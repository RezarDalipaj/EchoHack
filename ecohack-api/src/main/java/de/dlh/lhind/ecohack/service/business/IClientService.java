package de.dlh.lhind.ecohack.service.business;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.FilterDto;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.dto.response.QuizResponse;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.Client;

import java.util.List;

public interface IClientService {

    TokenDto save(ClientDto clientDto) throws BadRequestException, UnAuthorizedException;
    ClientDto update(ClientDto clientDto) throws BadRequestException;
    QuizResponse takeQuiz(QuestionnaireDto questionnaire, String username) throws BadRequestException;
    Integer getPoints(String username);
    ClientDto findById(Long clientId);

    Client findEntityById(Long clientId);

    ClientDto findByUsername(String username);
    Client findClientByUsername(String username);
    List<ClientDto> filterClients(FilterDto filterDto);
}
