package com.galero.repository;

import com.galero.model.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EditionRepository extends JpaRepository<Edition, Integer> {
    Optional<Edition> findByEditionNumber(Integer editionNumber);
    Optional<Edition> findByDate(LocalDate date);
}
