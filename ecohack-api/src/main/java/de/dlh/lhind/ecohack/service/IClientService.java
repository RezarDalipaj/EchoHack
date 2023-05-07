package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.Client;

public interface IClientService {


    Integer saveClientPoints(QuestionnaireDto questionnaire, Long clientId);
    TokenDto save(ClientDto clientDto) throws BadRequestException;
    Integer savePoints(QuestionnaireDto questionnaire, Long clientId);
    Integer getPoints(String username);
    ClientDto findById(Long clientId);
    ClientDto findByUsername(String username);
    Client findClientByUsername(String username);
}
