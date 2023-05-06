package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.service.IOrderService;
import de.dlh.lhind.ecohack.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IUserService userService;
    @Override
    public OrderDto save(OrderDto orderDto) {
        var user =
    }
}
