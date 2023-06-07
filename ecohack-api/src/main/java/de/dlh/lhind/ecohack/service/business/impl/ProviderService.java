package de.dlh.lhind.ecohack.service.business.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.mapper.ProviderMapper;
import de.dlh.lhind.ecohack.model.dto.ProviderDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.FoodProvider;
import de.dlh.lhind.ecohack.model.enumeration.Role;
import de.dlh.lhind.ecohack.repository.FoodProviderRepository;
import de.dlh.lhind.ecohack.service.security.IAuthService;
import de.dlh.lhind.ecohack.service.business.IProviderService;
import de.dlh.lhind.ecohack.service.business.IUserService;
import de.dlh.lhind.ecohack.util.filter.FilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderService implements IProviderService {

    private final FoodProviderRepository providerRepository;
    private final IAuthService authService;
    private final ProviderMapper providerMapper;
    private final IUserService userService;
    private final FilterUtil<FoodProvider> filterUtil = new FilterUtil<>();


    @Override
    @Transactional
    public TokenDto saveProvider(ProviderDto providerDto) throws BadRequestException, UnAuthorizedException {
        validateProviderRegister(providerDto);
        var provider = providerMapper.toProvider(providerDto);
        var userDto = userService.mapEntityToDto(provider.getUser());
        userDto.setRole(Role.PROVIDER.name());
        provider.setUser(userService.saveUser(userDto));
        providerRepository.save(provider);
        return authService.login(providerDto);
    }

    @Override
    public FoodProvider findByEmail(String email) {
        var provider = providerRepository.findByUsername(email);
        if (provider == null)
            throw new NullPointerException("Provider with username " + email + " doesnt exist");
        return provider;
    }

    private void validateProviderRegister(ProviderDto providerDto) throws BadRequestException {
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
