package de.dlh.lhind.ecohack.service.business;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.OrderDto;

public interface IOrderService {
    OrderDto save(OrderDto orderDto, String username) throws BadRequestException;
}
