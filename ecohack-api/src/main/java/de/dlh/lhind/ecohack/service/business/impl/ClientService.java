package de.dlh.lhind.ecohack.service.business.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.mapper.ClientMapper;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.FilterDto;
import de.dlh.lhind.ecohack.model.dto.QuizDto;
import de.dlh.lhind.ecohack.model.dto.request.QuestionnaireDto;
import de.dlh.lhind.ecohack.model.dto.request.ResultDto;
import de.dlh.lhind.ecohack.model.dto.response.QuizResponse;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.Client;
import de.dlh.lhind.ecohack.model.enumeration.PaymentMethod;
import de.dlh.lhind.ecohack.repository.ClientRepository;
import de.dlh.lhind.ecohack.repository.UserRepository;
import de.dlh.lhind.ecohack.service.business.IClientService;
import de.dlh.lhind.ecohack.service.business.IUserService;
import de.dlh.lhind.ecohack.service.security.IAuthService;
import de.dlh.lhind.ecohack.util.filter.FilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final IAuthService authService;
    private final IUserService userService;
    private final ClientMapper clientMapper;
    private final QuizDto quizDto;
    private final UserRepository userRepository;
    private final FilterUtil<Client> filterUtil = new FilterUtil<>();

    @Override
    @Transactional
    public QuizResponse takeQuiz(QuestionnaireDto questionnaire, String username) throws BadRequestException {
        var client = findClientByUsername(username);
        if (client.isTakenQuiz())
            throw new BadRequestException("You have taken the quiz once");
        var totalPoints = calculatePoints(questionnaire);
        client.setRankingPoints(totalPoints);
        client.setTakenQuiz(true);
        clientRepository.save(client);
        return QuizResponse.builder()
                .result("You got ".concat(totalPoints.toString()).concat(totalPoints <=1 ? " point" : " points"))
                .build();
    }

    private Integer calculatePoints(QuestionnaireDto questionnaire) throws BadRequestException {
        var questions = quizDto.getQuestions();
        var results = questionnaire.getResults();
        var resultSize = results.size();
        if (questions.size() != resultSize)
            throw new BadRequestException("Wrong results");
        Integer points = 0;
        var answers = results.stream()
                .map(ResultDto::getResult)
                .toList();
        for (int i = 0; i < resultSize; i++) {
            points += questions.get(i).getAnswers().get(answers.get(i)-1).getPoints();
        }
        return points/ resultSize;
    }

    @Override
    @Transactional
    public TokenDto save(ClientDto clientDto) throws BadRequestException {
        validateClientRegister(clientDto);
        var client = clientMapper.dtoToClient(clientDto);
        var userDto = userService.mapEntityToDto(client.getUser());
        client.setUser(userService.saveUser(userDto));
        clientRepository.save(client);
        return authService.login(clientDto);
    }

    @Override
    public ClientDto update(ClientDto clientDto) throws BadRequestException {
        var client = findEntityById(clientDto.getId());
        var userDto = clientMapper.clientDtoToUserDto(clientDto);
        client.setUser(userService.saveUser(userDto));
        if (clientDto.getName() != null)
            client.setName(clientDto.getName());
        if (clientDto.getLatitude() != 0)
            client.setLatitude(clientDto.getLatitude());
        if (clientDto.getLongtitude() != 0)
            client.setLongtitude(clientDto.getLongtitude());
        if (clientDto.getSurname() != null)
            client.setSurname(clientDto.getSurname());
        if (clientDto.getPaymentMethod() != null)
            client.setPaymentMethod(PaymentMethod.valueOf(clientDto.getPaymentMethod()));
        if (clientDto.getBalance() != 0)
            client.setBalance(clientDto.getBalance());
        return clientMapper.clientToDto(clientRepository.save(client));
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
        var client = clientRepository.findByUsername(username);
        if (client.isEmpty())
            throw new NullPointerException("Client with username " + username + " not found");
        return client.get();
    }

    @Override
    public List<ClientDto> filterClients(FilterDto filterDto) {

        var specification = filterUtil.filterWithAndEqualOperators(filterDto);

        var clients = clientRepository.findAll(specification);
        return clientMapper.clientsToClientsDto(clients);
    }

    private void validateClientRegister(ClientDto clientDto) throws BadRequestException {
        userService.validateUsername(clientDto.getUsername());
    }

}
