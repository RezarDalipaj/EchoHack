package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.ProviderDto;
import de.dlh.lhind.ecohack.model.dto.response.TokenDto;

public interface IProviderService {
    TokenDto saveProvider(ProviderDto providerDto) throws BadRequestException;
}
