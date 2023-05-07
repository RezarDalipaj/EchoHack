package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.dto.IngredientDto;
import de.dlh.lhind.ecohack.service.IIngredientService;
import de.dlh.lhind.ecohack.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {
    private final IIngredientService ingredientService;
    private final TokenUtil tokenUtil;


    @GetMapping()
    public ResponseEntity<List<IngredientDto>> findAll(){
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @PreAuthorize("hasAuthority('FOOD_PROVIDER')")
    @PostMapping()
    public ResponseEntity<IngredientDto> save(@RequestBody IngredientDto ingredientDto, HttpServletRequest request){
        return ResponseEntity.ok(ingredientService.save(ingredientDto, tokenUtil.usernameFromToken(request)));
    }
}
