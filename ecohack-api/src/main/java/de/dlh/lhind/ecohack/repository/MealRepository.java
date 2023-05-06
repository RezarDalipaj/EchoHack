package de.dlh.lhind.ecohack.repository;

import de.dlh.lhind.ecohack.model.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
}
