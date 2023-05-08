package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.mapper.ClientMapper;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.Client;
import de.dlh.lhind.ecohack.model.enumeration.Answers;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.ClientRepository;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.service.IClientService;
import de.dlh.lhind.ecohack.service.IUserService;
import de.dlh.lhind.ecohack.service.IValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final IAuthService authService;
    private final IUserService userService;
    private final IValidationService validationService;
    private final ClientMapper clientMapper;

    @Override
    public Integer savePoints(QuestionnaireDto questionnaire, Long clientId) {
        Integer totalPoints = 0;

        switch (questionnaire.getQuestionOneResult()) {
            case 1 -> totalPoints += Answers.QUESTION_ONE_1.getPoints();
            case 2 -> totalPoints += Answers.QUESTION_ONE_2.getPoints();
            case 3 -> totalPoints += Answers.QUESTION_ONE_3.getPoints();
            default -> totalPoints += 0;
        }

        switch (questionnaire.getQuestionTwoResult()) {
            case 1 -> totalPoints += Answers.QUESTION_TWO_1.getPoints();
            case 2 -> totalPoints += Answers.QUESTION_TWO_2.getPoints();
            case 3 -> totalPoints += Answers.QUESTION_TWO_3.getPoints();
            default -> totalPoints += 0;
        }

        switch (questionnaire.getQuestionThreeResult()) {
            case 1 -> totalPoints += Answers.QUESTION_THREE_1.getPoints();
            case 2 -> totalPoints += Answers.QUESTION_THREE_2.getPoints();
            case 3 -> totalPoints += Answers.QUESTION_THREE_3.getPoints();
            default -> totalPoints += 0;
        }

        switch (questionnaire.getQuestionFourResult()) {
            case 1 -> totalPoints += Answers.QUESTION_FOUR_1.getPoints();
            case 2 -> totalPoints += Answers.QUESTION_FOUR_2.getPoints();
            case 3 -> totalPoints += Answers.QUESTION_FOUR_3.getPoints();
            default -> totalPoints += 0;
        }

        //TODO: Update user points
        return totalPoints;

    }

    @Override
    public Integer saveClientPoints(QuestionnaireDto questionnaire, Long clientId) {
        return null;
    }

    @Override
    public TokenDto save(ClientDto clientDto) throws BadRequestException {
        validateRegister(clientDto);
        var client = clientMapper.dtoToClient(clientDto);
        var user = client.getUser();
        client.setUser(userService.save(user, Role.CLIENT));
        clientRepository.save(client);
        var login = new LoginDto();
        login.setUsername(clientDto.getUsername());
        login.setPassword(clientDto.getPassword());
        return authService.login(login);
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
        return null;
    }

    @Override
    public ClientDto findByUsername(String username) {
        return clientMapper.clientToDto(findClientByUsername(username));
    }

    @Override
    public Client findClientByUsername(String username) {
        var client = clientRepository.findByUser_Email(username);
        if (client.isEmpty())
            throw new NullPointerException("Client not found");
        return client.get();
    }

    private void validateRegister(ClientDto clientDto) throws BadRequestException {
        validationService.validateUsername(clientDto.getUsername());
    }

}
