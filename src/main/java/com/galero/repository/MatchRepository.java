package com.galero.repository;

import com.galero.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByEditionEditionId(Integer editionId);
    List<Match> findByTeam1TeamId(Integer teamId);
    List<Match> findByTeam2TeamId(Integer teamId);
    List<Match> findByMatchType(String matchType);

    @Query("SELECT m FROM Match m WHERE m.edition.editionId = :editionId " +
            "AND m.matchType IN ('big_final', 'small_final') " +
            "AND (m.team1.teamId = :teamId OR m.team2.teamId = :teamId)")
    Optional<Match> findFinalMatchByEditionAndTeam(@Param("editionId") Integer editionId, @Param("teamId") Integer teamId);
}
