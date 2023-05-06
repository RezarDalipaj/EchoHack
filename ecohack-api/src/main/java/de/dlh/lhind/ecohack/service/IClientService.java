package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;

public interface IClientService {

    public Integer saveClientPoints(QuestionnaireDto questionnaire, Long clientId);

}
