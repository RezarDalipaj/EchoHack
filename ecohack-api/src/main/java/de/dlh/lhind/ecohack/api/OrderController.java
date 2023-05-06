package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.dto.OrderDto;
import de.dlh.lhind.ecohack.security.config.JwtTokenUtil;
import de.dlh.lhind.ecohack.service.IOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderDto orderDto, HttpServletRequest request) throws Exception{
        String username = usernameFromToken(request);
        orderDto.setUsername(username);
        return ResponseEntity.ok(orderService.save(orderDto));
    }

    private String usernameFromToken(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
