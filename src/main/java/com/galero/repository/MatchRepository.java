package com.galero.repository;

import com.galero.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByEditionEditionId(Integer editionId);
    List<Match> findByTeam1TeamId(Integer teamId);
    List<Match> findByTeam2TeamId(Integer teamId);
    List<Match> findByMatchType(String matchType);
}
