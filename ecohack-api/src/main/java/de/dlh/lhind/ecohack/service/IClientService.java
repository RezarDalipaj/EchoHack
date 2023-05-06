package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;

public interface IClientService {


    public Integer saveClientPoints(QuestionnaireDto questionnaire, Long clientId);
    TokenDto save(ClientDto clientDto) throws BadRequestException;
    public Integer savePoints(QuestionnaireDto questionnaire, Long clientId);
    public Integer getPoints(String username);
    public ClientDto findById(Long clientId);
    public ClientDto findByUsername(String username);

}
