package de.dlh.lhind.ecohack.service;

import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.dto.TagDto;

import java.util.List;

public interface IMealService {

    public List<MealDto> findAllByTags(List<TagDto> tags, int pageSize, int pageNumber);
}
