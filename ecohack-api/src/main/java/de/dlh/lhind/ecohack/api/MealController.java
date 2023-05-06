package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.dto.ClientDto;
import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.dto.TagDto;
import de.dlh.lhind.ecohack.service.impl.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @GetMapping()
    public ResponseEntity<List<MealDto>> findAllByTags(@RequestBody List<TagDto> tags, @RequestParam int pageSize, @RequestParam int pageNumber){
        return ResponseEntity.ok(mealService.findAllByTags(tags, pageSize, pageNumber));
    }
}
