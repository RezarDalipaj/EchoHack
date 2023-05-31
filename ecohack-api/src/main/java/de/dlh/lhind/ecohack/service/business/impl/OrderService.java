package de.dlh.lhind.ecohack.service.business.impl;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.model.entity.Meal;
import de.dlh.lhind.ecohack.model.entity.Order;
import de.dlh.lhind.ecohack.model.entity.OrderDetail;
import de.dlh.lhind.ecohack.model.enumeration.OrderStatus;
import de.dlh.lhind.ecohack.model.enumeration.PaymentMethod;
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
        var prices = order.getMeals().stream()
                .map(Meal::getPrice)
                .toList();
        var price = calculatePrice(prices);
        order.setOrderDetail(setOrderDetails(client.getPaymentMethod(), price));
        var clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setBalance(client.getBalance() - price);
        clientService.update(clientDto);
        orderRepository.save(order);
    }

    private OrderDetail setOrderDetails(PaymentMethod paymentMethod, Double price){
        var details = new OrderDetail();
        details.setStatus(OrderStatus.ACCEPTED);
        details.setPaymentMethod(paymentMethod);
        details.setPrice(price);
        details.setOrderDate(LocalDateTime.now());
        return details;
    }

    private Double calculatePrice(List<Double> prices) {
        Double finalPrice = 0D;
        for (var price : prices) {
            finalPrice += price;
        }
        return finalPrice;
    }
}
