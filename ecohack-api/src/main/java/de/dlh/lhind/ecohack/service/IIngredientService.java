package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.IngredientDto;

import java.util.List;

public interface IIngredientService {
    public IngredientDto save(IngredientDto ingredient, String username);
    public List<IngredientDto> findAll();
}
