package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.service.IOrderService;
import de.dlh.lhind.ecohack.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final TokenUtil tokenUtil;

    @PreAuthorize("hasAuthority('FOOD_PROVIDER')")
    @PostMapping
    public void saveOrder(@Valid @RequestBody OrderDto orderDto, HttpServletRequest request) throws Exception {
        orderService.save(orderDto, tokenUtil.usernameFromToken(request));
    }
}
