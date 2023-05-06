package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.mapper.IngredientMapper;
import de.dlh.lhind.ecohack.model.dto.IngredientDto;
import de.dlh.lhind.ecohack.model.entity.Ingredient;
import de.dlh.lhind.ecohack.repository.IngredientRepository;
import de.dlh.lhind.ecohack.service.IIngredientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IngredientService implements IIngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    @Override
    public IngredientDto save(IngredientDto ingredient) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findByName(ingredient.getName());
        if (ingredientOptional.isPresent()){
            ingredientOptional.get().setPoints(ingredient.getPoints());
            return ingredientMapper.toDto(ingredientRepository.save(ingredientOptional.get()));
        } else {
            Ingredient ingredient1 = new Ingredient();
            ingredient1.setName(ingredient.getName());
            ingredient1.setPoints(ingredient.getPoints());

            return ingredientMapper.toDto(ingredientRepository.save(ingredient1));
        }
    }

    @Override
    public List<IngredientDto> findAll() {
        return ingredientMapper.toDtoList(ingredientRepository.findAll());
    }
}
