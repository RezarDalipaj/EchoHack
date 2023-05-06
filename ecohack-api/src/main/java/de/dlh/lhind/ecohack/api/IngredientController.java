package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.dto.IngredientDto;
import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.service.IIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {
    private final IIngredientService ingredientService;


    @GetMapping()
    public ResponseEntity<List<IngredientDto>> findAll(){
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @PostMapping()
    public ResponseEntity<IngredientDto> save(@RequestBody IngredientDto ingredientDto){
        return ResponseEntity.ok(ingredientService.save(ingredientDto));
    }
}
