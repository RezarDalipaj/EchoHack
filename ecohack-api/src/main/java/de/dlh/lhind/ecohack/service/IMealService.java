package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.dto.TagDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IMealService {

    public List<MealDto> findAllByClientUsername(String username, int pageSize, int pageNumber);
    public Void uploadImage(MultipartFile image, Long mealId) throws IOException;
    public MealDto findById(Long id);
    public MealDto save(MealDto mealDto);
    public MealDto updateImage(String image, Long mealId);
}
