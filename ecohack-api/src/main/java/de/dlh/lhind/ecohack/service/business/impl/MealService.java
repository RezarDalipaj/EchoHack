package de.dlh.lhind.ecohack.service.business.impl;

import de.dlh.lhind.ecohack.mapper.MealMapper;
import de.dlh.lhind.ecohack.model.dto.IngredientDto;
import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.dto.NutritionDto;
import de.dlh.lhind.ecohack.model.entity.Ingredient;
import de.dlh.lhind.ecohack.model.entity.Meal;
import de.dlh.lhind.ecohack.model.entity.Nutrition;
import de.dlh.lhind.ecohack.repository.IngredientRepository;
import de.dlh.lhind.ecohack.repository.MealRepository;
import de.dlh.lhind.ecohack.service.business.IClientService;
import de.dlh.lhind.ecohack.service.business.IMealService;
import de.dlh.lhind.ecohack.service.business.IProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class MealService implements IMealService {

    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final IClientService clientService;
    private final IProviderService providerService;
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
    public List<MealDto> findAllByProviderUsername(String username, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return mealMapper.toMealDtoList(mealRepository.findAllByFoodProvider_User_Email(username, pageable));
    }


    @Override
    @Transactional
    public void uploadImage(MultipartFile image, Long mealId) throws IOException {

        byte[] imageData = image.getBytes();
        String imageBase64 = Base64.getEncoder().encodeToString(imageData);

        updateImage(imageBase64, mealId);
    }

    @Override
    public MealDto findById(Long id) {
        return mealMapper.toMealDto(findEntityById(id));
    }

    @Override
    public Meal findEntityById(Long id) {
        Optional<Meal> mealOptional = mealRepository.findById(id);
        if (mealOptional.isEmpty())
            throw new NullPointerException("Meal with id " + id + " doesn't exist");
        return mealOptional.get();
    }

    @Override
    @Transactional
    public MealDto save(MealDto mealDto, String username) {
        var provider = providerService.findByEmail(username);
        Meal meal = new Meal();
        meal.setFoodProvider(provider);

        meal.setName(mealDto.getName());
        meal.setPrice(mealDto.getPrice());

        List<Ingredient> ingredients = new ArrayList<>();
        if (isNotEmpty(mealDto.getIngredients())) {
            for (IngredientDto ingredientDto : mealDto.getIngredients()) {
                Optional<Ingredient> ingredientOptional = ingredientRepository.findById(ingredientDto.getId());
                ingredientOptional.ifPresent(ingredients::add);
            }

            Integer totalPoints = getTotalPoints(ingredients);
            meal.setTotalPoints(totalPoints);
        }

        List<Nutrition> nutritions = new ArrayList<>();
        if (isNotEmpty(mealDto.getNutritions())) {
            for (NutritionDto nutritionDto : mealDto.getNutritions()) {
                Nutrition nutrition = new Nutrition();
                nutrition.setName(nutritionDto.getName());
                nutrition.setMeal(meal);
                nutrition.setAmount(nutritionDto.getAmount());
                nutritions.add(nutrition);
            }
        }

        meal.setIngredients(ingredients);
        meal.setNutritions(nutritions);

        return mealMapper.toMealDto(mealRepository.save(meal));
    }

    @Override
    @Transactional
    public MealDto updateImage(String image, Long mealId) {
        Optional<Meal> mealOptional = mealRepository.findById(mealId);
        if (mealOptional.isPresent()){
            Meal meal = mealOptional.get();
            meal.setImage(image);
            mealRepository.save(meal);
            return mealMapper.toMealDto(meal);
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
