package de.dlh.lhind.ecohack.repository;

import de.dlh.lhind.ecohack.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String token);
    boolean existsByValue(String value);
}
