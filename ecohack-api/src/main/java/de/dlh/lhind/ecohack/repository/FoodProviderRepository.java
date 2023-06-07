package de.dlh.lhind.ecohack.repository;

import de.dlh.lhind.ecohack.model.entity.FoodProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodProviderRepository extends JpaRepository<FoodProvider, Long>, JpaSpecificationExecutor<FoodProvider> {
    boolean existsByName(String name);
    boolean existsByNipt(String nipt);
    FoodProvider findByUsername(String email);
}
