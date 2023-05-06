package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.mapper.MealMapper;
import de.dlh.lhind.ecohack.mapper.TagMapper;
import de.dlh.lhind.ecohack.model.dto.IngredientDto;
import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.dto.NutritionDto;
import de.dlh.lhind.ecohack.model.dto.TagDto;
import de.dlh.lhind.ecohack.model.entity.Ingredient;
import de.dlh.lhind.ecohack.model.entity.Meal;
import de.dlh.lhind.ecohack.model.entity.Nutrition;
import de.dlh.lhind.ecohack.repository.IngredientRepository;
import de.dlh.lhind.ecohack.repository.MealRepository;
import de.dlh.lhind.ecohack.repository.NutritionRepository;
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
    private final NutritionRepository nutritionRepository;
    private final ClientService clientService;
    private final TagMapper tagMapper;
    private final MealMapper mealMapper;


    @Override
    public List<MealDto> findAllByClientUsername(String username, int pageSize, int pageNumber) {
        Integer rankingPoints = clientService.getPoints(username);
        int min = rankingPoints - 10;
        int max = rankingPoints + 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return mealMapper.toMealDtoList(mealRepository.findByTotalPointsBetween(min, max, pageable));
    }


    @Override
    public Void uploadImage(MultipartFile image, Long mealId) throws IOException {

        byte[] imageData = image.getBytes();
        String imageBase64 = Base64.getEncoder().encodeToString(imageData);

        updateImage(imageBase64, mealId);
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
        Meal meal = new Meal();
        meal.setName(mealDto.getName());

        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientDto ingredientDto : mealDto.getIngredients()){
            Optional<Ingredient> ingredientOptional = ingredientRepository.findById(ingredientDto.getId());
            ingredientOptional.ifPresent(ingredients::add);
        }

        Integer totalPoints = getTotalPoints(ingredients);

        List<Nutrition> nutritions = new ArrayList<>();
        for (NutritionDto nutritionDto : mealDto.getNutritions()){
            Optional<Nutrition> nutritionOptional = nutritionRepository.findById(nutritionDto.getId());
            nutritionOptional.ifPresent(nutritions::add);
        }

        meal.setTotalPoints(totalPoints);
        meal.setIngredients(ingredients);
        meal.setNutritions(nutritions);

        return mealMapper.toMealdDto(mealRepository.save(meal));
    }

    @Override
    public MealDto updateImage(String image, Long mealId) {
        Optional<Meal> mealOptional = mealRepository.findById(mealId);
        if (mealOptional.isPresent()){
            Meal meal = mealOptional.get();
            meal.setImage(image);
            mealRepository.save(meal);
            return mealMapper.toMealdDto(meal);
        } else {
            throw new NullPointerException("Meal with id: " + mealId + " was not found!");
        }
    }

    private Integer getTotalPoints(List<Ingredient> ingredients){
        Integer totalPoints = 0;
        if (ingredients.isEmpty()){
            return totalPoints;
        }

        for (Ingredient ingredient : ingredients){
            totalPoints += ingredient.getPoints();
        }
        return totalPoints/ingredients.size();
    }


}
