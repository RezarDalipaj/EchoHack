package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.mapper.ProviderMapper;
import de.dlh.lhind.ecohack.model.dto.ProviderDto;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.FoodProvider;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.FoodProviderRepository;
import de.dlh.lhind.ecohack.service.IAuthService;
import de.dlh.lhind.ecohack.service.IProviderService;
import de.dlh.lhind.ecohack.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderService implements IProviderService {

    private final FoodProviderRepository providerRepository;
    private final IAuthService authService;
    private final ProviderMapper providerMapper;
    private final IUserService userService;

    @Override
    public TokenDto saveProvider(ProviderDto providerDto) throws BadRequestException, UnAuthorizedException {
        validateProvider(providerDto);
        var provider = providerMapper.toProvider(providerDto);
        var user = provider.getUser();
        provider.setUser(userService.save(user, Role.PROVIDER));
        providerRepository.save(provider);
        var login = new LoginDto();
        login.setUsername(providerDto.getUsername());
        login.setPassword(providerDto.getPassword());
        return authService.login(login);
    }

    @Override
    public FoodProvider findByEmail(String email) {
        var provider = providerRepository.findByUser_Email(email);
        if (provider == null)
            throw new NullPointerException("Provider with username " + email + " doesnt exist");
        return provider;
    }

    private void validateProvider(ProviderDto providerDto) throws BadRequestException {
        userService.validateUsername(providerDto.getUsername());
        validateName(providerDto.getName());
        validateNipt(providerDto.getNipt());
    }

    private void validateNipt(String nipt) throws BadRequestException {
        if (providerRepository.existsByNipt(nipt))
            throw new BadRequestException("Provider with nipt " + nipt + " already exists");
    }

    private void validateName(String name) throws BadRequestException {
        if (providerRepository.existsByName(name))
            throw new BadRequestException("This provider already exists");
    }
}
