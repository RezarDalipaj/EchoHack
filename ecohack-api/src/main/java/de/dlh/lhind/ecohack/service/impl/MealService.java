package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.mapper.MealMapper;
import de.dlh.lhind.ecohack.mapper.TagMapper;
import de.dlh.lhind.ecohack.model.dto.IngredientDto;
import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.dto.TagDto;
import de.dlh.lhind.ecohack.model.entity.Ingredient;
import de.dlh.lhind.ecohack.model.entity.Meal;
import de.dlh.lhind.ecohack.repository.IngredientRepository;
import de.dlh.lhind.ecohack.repository.MealRepository;
import de.dlh.lhind.ecohack.service.IMealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealService implements IMealService {

    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final ClientService clientService;
    private final TagMapper tagMapper;
    private final MealMapper mealMapper;

    @Override
    public List<MealDto> findAllByTags(List<TagDto> tags, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return mealMapper.toMealDtoList(mealRepository.findAllByTags(tagMapper.toTagList(tags), pageable));
    }

    @Override
    public List<MealDto> findAllByClientUsername(String username, int pageSize, int pageNumber) {
        Integer rankingPoints = clientService.getPoints(username);
        int min = rankingPoints - 10;
        int max = rankingPoints + 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return mealMapper.toMealDtoList(mealRepository.findByTargetClientPointsBetween(min, max, pageable));
    }


    @Override
    public Void uploadImage(MultipartFile image, Long mealId) throws IOException {
        MealDto meal = findById(mealId);

        byte[] imageData = image.getBytes();
        String imageBase64 = Base64.getEncoder().encodeToString(imageData);
        meal.setImage(imageBase64);

        return null;
    }

    @Override
    public MealDto findById(Long id) {
        Optional<Meal> mealOptional = mealRepository.findById(id);
        if (mealOptional.isPresent()){
            return mealMapper.toMealdDto(mealOptional.get());
        } else {
            throw new NullPointerException("Meal with id: " + id + " does not exist");
        }

    }

    @Override
    public MealDto save(MealDto mealDto) {
        if (mealDto.getId() == null){
            Meal meal = new Meal();
            meal.setName(mealDto.getName());

            List<Ingredient> ingredients = new ArrayList<>();
            for (IngredientDto ingredientDto : mealDto.getIngredients()){
                Optional<Ingredient> ingredientOptional = ingredientRepository.findById(ingredientDto.getId());
                ingredientOptional.ifPresent(ingredients::add);
            }

            meal.setIngredients(ingredients);
        }
        return null;
    }


}
