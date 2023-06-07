package de.dlh.lhind.ecohack.repository;

import de.dlh.lhind.ecohack.model.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long>, JpaSpecificationExecutor<Nutrition> {
}
