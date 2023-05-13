package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.mapper.ClientMapper;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.QuizDto;
import de.dlh.lhind.ecohack.model.dto.QuizResponse;
import de.dlh.lhind.ecohack.model.dto.ResultDto;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.Client;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.ClientRepository;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.service.IClientService;
import de.dlh.lhind.ecohack.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final IAuthService authService;
    private final IUserService userService;
    private final ClientMapper clientMapper;
    private final QuizDto quizDto;

    @Override
    public QuizResponse takeQuiz(QuestionnaireDto questionnaire, String username) throws BadRequestException {
        var client = findClientByUsername(username);
        if (client.isTakenQuiz())
            throw new BadRequestException("You have taken the quiz once");
        var totalPoints = calculatePoints(questionnaire);
        client.setRankingPoints(totalPoints);
        client.setTakenQuiz(true);
        clientRepository.save(client);
        return QuizResponse.builder()
                .result("You got " + totalPoints + " points")
                .build();

    }

    private Integer calculatePoints(QuestionnaireDto questionnaire) throws BadRequestException {
        var questions = quizDto.getQuestions();
        var results = questionnaire.getResults();
        if (questions.size() != results.size())
            throw new BadRequestException("Wrong results");
        Integer points = 0;
        var answers = results.stream().map(ResultDto::getResult).toList();
        for (int i = 0; i < results.size(); i++) {
            points += questions.get(i).getAnswers().get(answers.get(i)-1).getPoints();
        }
        return points/ results.size();
    }

    @Override
    public TokenDto save(ClientDto clientDto) throws BadRequestException, UnAuthorizedException {
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
        return clientMapper.clientToDto(findEntityById(clientId));
    }

    @Override
    public Client findEntityById(Long clientId){
        var client = clientRepository.findById(clientId);
        if (client.isEmpty())
            throw new NullPointerException("Client with id " + clientId + " not found");
        return client.get();
    }

    @Override
    public ClientDto findByUsername(String username) {
        return clientMapper.clientToDto(findClientByUsername(username));
    }

    @Override
    public Client findClientByUsername(String username) {
        var client = clientRepository.findByUser_Email(username);
        if (client.isEmpty())
            throw new NullPointerException("Client with username " + username + " not found");
        return client.get();
    }

    private void validateRegister(ClientDto clientDto) throws BadRequestException {
        userService.validateUsername(clientDto.getUsername());
    }

}
