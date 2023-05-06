package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TestController {
    private final IUserService userService;

    @GetMapping("/all/clients")
    public ResponseEntity<List<User>> saveClient(){
        return ResponseEntity.ok(userService.findAll());
    }
}
