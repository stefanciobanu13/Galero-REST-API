package com.galero.repository;

import com.galero.dto.PlayerEditionWinsDTO;
import com.galero.dto.PlayerAllTimeScoresDTO;
import com.galero.dto.PlayerPlacementStatsDTO;
import com.galero.model.Edition;
import com.galero.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT DISTINCT e FROM Edition e " +
            "JOIN Team t ON t.edition.editionId = e.editionId " +
            "JOIN TeamPlayer tp ON tp.team.teamId = t.teamId " +
            "WHERE tp.player.playerId = :playerId " +
            "ORDER BY e.editionNumber DESC")
    List<Edition> findEditionsWherePlayerParticipated(@Param("playerId") Integer playerId);

    @Query("SELECT NEW com.galero.dto.PlayerEditionWinsDTO(" +
            "p.playerId, p.firstName, p.lastName, p.grade, " +
            "COUNT(DISTINCT CASE WHEN m.matchType = com.galero.model.Match$MatchType.big_final " +
            "AND ((m.team1.teamId = t.teamId AND m.team1Score > m.team2Score) " +
            "OR (m.team2.teamId = t.teamId AND m.team2Score > m.team1Score)) " +
            "THEN m.edition.editionId END), " +
            "COUNT(DISTINCT e.editionId)) " +
            "FROM Player p " +
            "JOIN TeamPlayer tp ON tp.player.playerId = p.playerId " +
            "JOIN Team t ON tp.team.teamId = t.teamId " +
            "LEFT JOIN Match m ON (m.team1.teamId = t.teamId OR m.team2.teamId = t.teamId) " +
            "JOIN Edition e ON t.edition.editionId = e.editionId " +
            "GROUP BY p.playerId, p.firstName, p.lastName, p.grade " +
            "HAVING COUNT(DISTINCT CASE WHEN m.matchType = com.galero.model.Match$MatchType.big_final " +
            "AND ((m.team1.teamId = t.teamId AND m.team1Score > m.team2Score) " +
            "OR (m.team2.teamId = t.teamId AND m.team2Score > m.team1Score)) " +
            "THEN m.edition.editionId END) > 0 " +
            "ORDER BY COUNT(DISTINCT CASE WHEN m.matchType = com.galero.model.Match$MatchType.big_final " +
            "AND ((m.team1.teamId = t.teamId AND m.team1Score > m.team2Score) " +
            "OR (m.team2.teamId = t.teamId AND m.team2Score > m.team1Score)) " +
            "THEN m.edition.editionId END) DESC")
    List<PlayerEditionWinsDTO> findTopPlayersByEditionWins();

    @Query("SELECT NEW com.galero.dto.PlayerAllTimeScoresDTO(" +
            "p.playerId, p.firstName, p.lastName, p.grade, COUNT(g.goalId)) " +
            "FROM Player p " +
            "LEFT JOIN Goal g ON g.player.playerId = p.playerId " +
            "GROUP BY p.playerId, p.firstName, p.lastName, p.grade " +
            "HAVING COUNT(g.goalId) > 0 " +
            "ORDER BY COUNT(g.goalId) DESC")
    List<PlayerAllTimeScoresDTO> findTopPlayersByAllTimeScores();

    @Query("SELECT NEW com.galero.dto.PlayerPlacementStatsDTO(" +
            "p.playerId, p.firstName, p.lastName, p.grade, " +
            "SUM(CASE WHEN placements.placement = 1 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN placements.placement = 2 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN placements.placement = 3 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN placements.placement = 4 THEN 1 ELSE 0 END), " +
            "COUNT(DISTINCT placements.editionId)) " +
            "FROM Player p " +
            "JOIN TeamPlayer tp ON tp.player.playerId = p.playerId " +
            "JOIN Team t ON tp.team.teamId = t.teamId " +
            "JOIN Edition e ON t.edition.editionId = e.editionId " +
            "LEFT JOIN (" +
            "  SELECT e2.editionId as editionId, t2.teamId as teamId, " +
            "  CASE " +
            "    WHEN m_big.matchType = com.galero.model.Match$MatchType.big_final " +
            "    AND ((m_big.team1.teamId = t2.teamId AND m_big.team1Score > m_big.team2Score) " +
            "    OR (m_big.team2.teamId = t2.teamId AND m_big.team2Score > m_big.team1Score)) THEN 1 " +
            "    WHEN m_big.matchType = com.galero.model.Match$MatchType.big_final " +
            "    AND ((m_big.team1.teamId = t2.teamId AND m_big.team1Score < m_big.team2Score) " +
            "    OR (m_big.team2.teamId = t2.teamId AND m_big.team2Score < m_big.team1Score)) THEN 2 " +
            "    WHEN m_small.matchType = com.galero.model.Match$MatchType.small_final " +
            "    AND ((m_small.team1.teamId = t2.teamId AND m_small.team1Score > m_small.team2Score) " +
            "    OR (m_small.team2.teamId = t2.teamId AND m_small.team2Score > m_small.team1Score)) THEN 3 " +
            "    WHEN m_small.matchType = com.galero.model.Match$MatchType.small_final " +
            "    AND ((m_small.team1.teamId = t2.teamId AND m_small.team1Score < m_small.team2Score) " +
            "    OR (m_small.team2.teamId = t2.teamId AND m_small.team2Score < m_small.team1Score)) THEN 4 " +
            "    ELSE NULL " +
            "  END as placement " +
            "  FROM Team t2 " +
            "  JOIN Edition e2 ON t2.edition.editionId = e2.editionId " +
            "  LEFT JOIN Match m_big ON m_big.edition.editionId = e2.editionId " +
            "  AND m_big.matchType = com.galero.model.Match$MatchType.big_final " +
            "  AND (m_big.team1.teamId = t2.teamId OR m_big.team2.teamId = t2.teamId) " +
            "  LEFT JOIN Match m_small ON m_small.edition.editionId = e2.editionId " +
            "  AND m_small.matchType = com.galero.model.Match$MatchType.small_final " +
            "  AND (m_small.team1.teamId = t2.teamId OR m_small.team2.teamId = t2.teamId) " +
            ") placements ON placements.teamId = t.teamId AND placements.editionId = e.editionId " +
            "GROUP BY p.playerId, p.firstName, p.lastName, p.grade " +
            "ORDER BY SUM(CASE WHEN placements.placement = 1 THEN 1 ELSE 0 END) DESC")
    List<PlayerPlacementStatsDTO> findPlayersPlacementStats();

    @Query("SELECT NEW com.galero.dto.PlayerPlacementStatsDTO(" +
            "p.playerId, p.firstName, p.lastName, p.grade, " +
            "SUM(CASE WHEN placements.placement = 1 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN placements.placement = 2 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN placements.placement = 3 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN placements.placement = 4 THEN 1 ELSE 0 END), " +
            "COUNT(DISTINCT placements.editionId)) " +
            "FROM Player p " +
            "JOIN TeamPlayer tp ON tp.player.playerId = p.playerId " +
            "JOIN Team t ON tp.team.teamId = t.teamId " +
            "JOIN Edition e ON t.edition.editionId = e.editionId " +
            "LEFT JOIN (" +
            "  SELECT e2.editionId as editionId, t2.teamId as teamId, " +
            "  CASE " +
            "    WHEN m_big.matchType = com.galero.model.Match$MatchType.big_final " +
            "    AND ((m_big.team1.teamId = t2.teamId AND m_big.team1Score > m_big.team2Score) " +
            "    OR (m_big.team2.teamId = t2.teamId AND m_big.team2Score > m_big.team1Score)) THEN 1 " +
            "    WHEN m_big.matchType = com.galero.model.Match$MatchType.big_final " +
            "    AND ((m_big.team1.teamId = t2.teamId AND m_big.team1Score < m_big.team2Score) " +
            "    OR (m_big.team2.teamId = t2.teamId AND m_big.team2Score < m_big.team1Score)) THEN 2 " +
            "    WHEN m_small.matchType = com.galero.model.Match$MatchType.small_final " +
            "    AND ((m_small.team1.teamId = t2.teamId AND m_small.team1Score > m_small.team2Score) " +
            "    OR (m_small.team2.teamId = t2.teamId AND m_small.team2Score > m_small.team1Score)) THEN 3 " +
            "    WHEN m_small.matchType = com.galero.model.Match$MatchType.small_final " +
            "    AND ((m_small.team1.teamId = t2.teamId AND m_small.team1Score < m_small.team2Score) " +
            "    OR (m_small.team2.teamId = t2.teamId AND m_small.team2Score < m_small.team1Score)) THEN 4 " +
            "    ELSE NULL " +
            "  END as placement " +
            "  FROM Team t2 " +
            "  JOIN Edition e2 ON t2.edition.editionId = e2.editionId " +
            "  LEFT JOIN Match m_big ON m_big.edition.editionId = e2.editionId " +
            "  AND m_big.matchType = com.galero.model.Match$MatchType.big_final " +
            "  AND (m_big.team1.teamId = t2.teamId OR m_big.team2.teamId = t2.teamId) " +
            "  LEFT JOIN Match m_small ON m_small.edition.editionId = e2.editionId " +
            "  AND m_small.matchType = com.galero.model.Match$MatchType.small_final " +
            "  AND (m_small.team1.teamId = t2.teamId OR m_small.team2.teamId = t2.teamId) " +
            ") placements ON placements.teamId = t.teamId AND placements.editionId = e.editionId " +
            "WHERE p.playerId = :playerId " +
            "GROUP BY p.playerId, p.firstName, p.lastName, p.grade")
    Optional<PlayerPlacementStatsDTO> findPlayerPlacementStats(@Param("playerId") Integer playerId);
}

