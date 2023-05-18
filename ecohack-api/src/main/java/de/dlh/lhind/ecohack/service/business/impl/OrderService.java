package de.dlh.lhind.ecohack.service.business.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.model.entity.Client;
import de.dlh.lhind.ecohack.model.entity.Meal;
import de.dlh.lhind.ecohack.model.entity.Order;
import de.dlh.lhind.ecohack.model.entity.OrderDetail;
import de.dlh.lhind.ecohack.model.enumeration.OrderStatus;
import de.dlh.lhind.ecohack.repository.OrderRepository;
import de.dlh.lhind.ecohack.service.business.IClientService;
import de.dlh.lhind.ecohack.service.business.IMealService;
import de.dlh.lhind.ecohack.service.business.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IClientService clientService;
    private final IMealService mealService;
    private final OrderRepository orderRepository;
    @Override
    @Transactional
    public void save(OrderDto orderDto, String username) throws BadRequestException {
        var client = clientService.findClientByUsername(username);
        var order = new Order();
        order.setClient(client);
        order.setComment(orderDto.getComment());
        order.setMeals(new ArrayList<>());
        for (var meal : orderDto.getMeals()) {
            var mealEntity = mealService.findEntityById(meal.getMealId());
            order.getMeals().add(mealEntity);
        }
        order.setOrderDetail(setOrderDetails(client, order.getMeals()));
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
    }

    private OrderDetail setOrderDetails(Client client, List<Meal> meals){
        var details = new OrderDetail();
        details.setStatus(OrderStatus.ACCEPTED);
        details.setPaymentMethod(client.getPaymentMethod());
        details.setPrice(calculatePrice(meals));
        return details;
    }

    private Double calculatePrice(List<Meal> meals) {
        Double price = 0D;
        for (var meal : meals) {
            price += meal.getPrice();
        }
        return price;
    }
}
