package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.OrderDto;

public interface IOrderService {
    void save(OrderDto orderDto, String username) throws BadRequestException;
}
