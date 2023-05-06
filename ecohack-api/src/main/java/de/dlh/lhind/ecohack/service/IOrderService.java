package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.OrderDto;

public interface IOrderService {
    OrderDto save(OrderDto orderDto);
}
