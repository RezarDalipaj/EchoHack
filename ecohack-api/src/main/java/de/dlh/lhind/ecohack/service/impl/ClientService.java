package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.entity.Client;
import de.dlh.lhind.ecohack.model.enumeration.Answers;
import de.dlh.lhind.ecohack.repository.ClientRepository;
import de.dlh.lhind.ecohack.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;

    @Override
    public Integer saveClientPoints(QuestionnaireDto questionnaire, Long clientId) {
        Integer totalPoints = 0;

        switch (questionnaire.getQuestionOneResult()){
            case 1:
                totalPoints += Answers.QUESTION_ONE_1.getPoints();
            case 2:
                totalPoints += Answers.QUESTION_ONE_2.getPoints();
            case 3:
                totalPoints += Answers.QUESTION_ONE_3.getPoints();
        }

        switch (questionnaire.getQuestionTwoResult()){
            case 1:
                totalPoints += Answers.QUESTION_TWO_1.getPoints();
            case 2:
                totalPoints += Answers.QUESTION_TWO_2.getPoints();
            case 3:
                totalPoints += Answers.QUESTION_TWO_3.getPoints();
        }

        switch (questionnaire.getQuestionThreeResult()){
            case 1:
                totalPoints += Answers.QUESTION_THREE_1.getPoints();
            case 2:
                totalPoints += Answers.QUESTION_THREE_2.getPoints();
            case 3:
                totalPoints += Answers.QUESTION_THREE_3.getPoints();
        }

        switch (questionnaire.getQuestionFourResult()){
            case 1:
                totalPoints += Answers.QUESTION_FOUR_1.getPoints();
            case 2:
                totalPoints += Answers.QUESTION_FOUR_2.getPoints();
            case 3:
                totalPoints += Answers.QUESTION_FOUR_3.getPoints();
        }

        //TODO: Update user points
        return totalPoints;

    }




}
