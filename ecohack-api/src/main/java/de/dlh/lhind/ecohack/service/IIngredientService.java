package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.IngredientDto;

import java.util.List;

public interface IIngredientService {
    IngredientDto save(IngredientDto ingredient);
    List<IngredientDto> findAll();
}
