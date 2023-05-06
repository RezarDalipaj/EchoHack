package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.model.entity.Order;
import de.dlh.lhind.ecohack.repository.OrderRepository;
import de.dlh.lhind.ecohack.service.IClientService;
import de.dlh.lhind.ecohack.service.IMealService;
import de.dlh.lhind.ecohack.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IClientService clientService;
    private final IMealService mealService;
    private final OrderRepository orderRepository;
    @Override
    public void save(OrderDto orderDto) throws BadRequestException {
        var client = clientService.findClientByUsername(orderDto.getUsername());
        if (orderDto.getMeals() == null || orderDto.getMeals().isEmpty())
            throw new BadRequestException("Order cannot be empty");
        var order = new Order();
        order.setClient(client);
        order.setMeals(new ArrayList<>());
        for (var meal : orderDto.getMeals()) {
            var mealEntity = mealService.findEntityById(meal.getMealId());
            order.getMeals().add(mealEntity);
        }
        orderRepository.save(order);
    }
}
