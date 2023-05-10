package de.dlh.lhind.ecohack.repository;

import de.dlh.lhind.ecohack.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String token);
}
