package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.security.token.TokenProvider;
import de.dlh.lhind.ecohack.service.business.IOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final TokenProvider tokenProvider;

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDto orderDto, HttpServletRequest request) throws BadRequestException, UnAuthorizedException {
        return ResponseEntity.ok(orderService.save(orderDto, tokenProvider.getUsernameFromRequest(request)));
    }
}
