package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.mapper.ClientMapper;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
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
    private final ClientMapper clientMapper;

    @Override
    public Integer savePoints(QuestionnaireDto questionnaire, Long clientId) {
        Integer totalPoints = 0;

        switch (questionnaire.getQuestionOneResult()){
            case 1:
                totalPoints += Answers.QUESTION_ONE_1.getPoints();
                break;
            case 2:
                totalPoints += Answers.QUESTION_ONE_2.getPoints();
                break;
            case 3:
                totalPoints += Answers.QUESTION_ONE_3.getPoints();
                break;

        }

        switch (questionnaire.getQuestionTwoResult()){
            case 1:
                totalPoints += Answers.QUESTION_TWO_1.getPoints();
                break;
            case 2:
                totalPoints += Answers.QUESTION_TWO_2.getPoints();
                break;
            case 3:
                totalPoints += Answers.QUESTION_TWO_3.getPoints();
                break;
        }

        switch (questionnaire.getQuestionThreeResult()){
            case 1:
                totalPoints += Answers.QUESTION_THREE_1.getPoints();
                break;
            case 2:
                totalPoints += Answers.QUESTION_THREE_2.getPoints();
                break;
            case 3:
                totalPoints += Answers.QUESTION_THREE_3.getPoints();
                break;
        }

        switch (questionnaire.getQuestionFourResult()){
            case 1:
                totalPoints += Answers.QUESTION_FOUR_1.getPoints();
                break;
            case 2:
                totalPoints += Answers.QUESTION_FOUR_2.getPoints();
                break;
            case 3:
                totalPoints += Answers.QUESTION_FOUR_3.getPoints();
                break;
        }

        //TODO: Update user points
        return totalPoints;

    }

    @Override
    public Integer getPoints(String username) {
        ClientDto client = findByUsername(username);
        if (client.getRankingPoints() != null){
            return client.getRankingPoints();
        } else {
            throw new NullPointerException("Client has not completed the quiz");
        }
    }

    @Override
    public ClientDto findById(Long clientId) {
        Optional<Client> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()){
            return clientMapper.clientToDto(clientOptional.get());
        } else {
            throw new NullPointerException("Client with id: " + clientId + " was not found");
        }
    }

    @Override
    public ClientDto findByUsername(String username) {
        Optional<Client> clientOptional = clientRepository.findByUser_Email(username);
        if (clientOptional.isPresent()){
            return clientMapper.clientToDto(clientOptional.get());
        } else {
            throw new NullPointerException("Client with email: " + username + " was not found");
        }
    }


}
