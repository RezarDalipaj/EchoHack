package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.service.IMealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final IMealService mealService;

    @GetMapping()
    public ResponseEntity<List<MealDto>> findAllByClientId(@RequestParam String username, @RequestParam int pageSize, @RequestParam int pageNumber){
        return ResponseEntity.ok(mealService.findAllByClientUsername(username, pageSize, pageNumber));
    }
    @PutMapping()
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image, @RequestParam Long mealId) throws IOException {
        mealService.uploadImage(image, mealId);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping()
    public ResponseEntity<MealDto> save(@RequestBody MealDto meal){
        return ResponseEntity.ok(mealService.save(meal));
    }


}
