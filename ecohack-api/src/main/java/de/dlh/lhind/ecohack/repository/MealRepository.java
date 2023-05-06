package de.dlh.lhind.ecohack.repository;

import de.dlh.lhind.ecohack.model.entity.Meal;
import de.dlh.lhind.ecohack.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findAllByTags(List<Tag> tags, Pageable pageable);

}
