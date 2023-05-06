package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;

public interface IClientService {

    public Integer savePoints(QuestionnaireDto questionnaire, Long clientId);
    public Integer getPoints(String username);
    public ClientDto findById(Long clientId);
    public ClientDto findByUsername(String username);

}
