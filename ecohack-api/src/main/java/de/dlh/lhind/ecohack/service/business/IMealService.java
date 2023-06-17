package de.dlh.lhind.ecohack.service.business;

import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.entity.Meal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IMealService {

    List<MealDto> findAllByClientUsername(String username, int pageSize, int pageNumber);

    List<MealDto> findAllByProviderUsername(String username, int pageSize, int pageNumber);

    void uploadImage(MultipartFile image, Long mealId, String username) throws IOException;
    MealDto findById(Long id);
    Meal findEntityById(Long id);
    MealDto save(MealDto mealDto, String username);
    MealDto updateImage(String image, Long mealId);
}
