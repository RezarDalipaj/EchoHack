package de.dlh.lhind.ecohack.service.business;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.ProviderDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;
import de.dlh.lhind.ecohack.model.entity.FoodProvider;

public interface IProviderService {
    TokenDto saveProvider(ProviderDto providerDto) throws BadRequestException, UnAuthorizedException;
    FoodProvider findByEmail(String email);
}
