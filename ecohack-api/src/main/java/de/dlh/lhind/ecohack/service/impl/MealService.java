package de.dlh.lhind.ecohack.service.impl;

import de.dlh.lhind.ecohack.mapper.MealMapper;
import de.dlh.lhind.ecohack.mapper.TagMapper;
import de.dlh.lhind.ecohack.model.dto.MealDto;
import de.dlh.lhind.ecohack.model.dto.TagDto;
import de.dlh.lhind.ecohack.model.entity.Tag;
import de.dlh.lhind.ecohack.repository.MealRepository;
import de.dlh.lhind.ecohack.service.IMealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService implements IMealService {

    private final MealRepository mealRepository;
    private final TagMapper tagMapper;
    private final MealMapper mealMapper;

    @Override
    public List<MealDto> findAllByTags(List<TagDto> tags, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Tag> tagEntities = new ArrayList<>();

        return mealMapper.toMealDtoList(mealRepository.findAllByTags(tagMapper.toTagList(tags), pageable));
    }




}
