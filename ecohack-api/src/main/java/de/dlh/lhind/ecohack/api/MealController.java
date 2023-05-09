package de.dlh.lhind.ecohack.api;

import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.service.IMealService;
import de.dlh.lhind.ecohack.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final IMealService mealService;
    private final TokenUtil tokenUtil;

    @GetMapping()
    public ResponseEntity<List<MealDto>> findAllByClientId(@RequestParam String username, @RequestParam int pageSize, @RequestParam int pageNumber){
        return ResponseEntity.ok(mealService.findAllByClientUsername(username, pageSize, pageNumber));
    }
    @PutMapping("/image")
    public void uploadImage(@RequestParam("image") MultipartFile image, @RequestParam Long mealId) throws IOException {
        mealService.uploadImage(image, mealId);
    }

    @PostMapping()
    public ResponseEntity<MealDto> save(@Valid @RequestBody MealDto meal, HttpServletRequest request) {
        return ResponseEntity.ok(mealService.save(meal, tokenUtil.usernameFromToken(request)));
    }

    @GetMapping("/provider")
    @PreAuthorize("hasAuthority('FOOD_PROVIDER')")
    public ResponseEntity<List<MealDto>> findAllByProviderId(@RequestParam int pageSize, @RequestParam int pageNumber
            , HttpServletRequest request) {
        return ResponseEntity.ok(mealService.findAllByProviderUsername(tokenUtil.usernameFromToken
                (request), pageSize, pageNumber));
    }


}
