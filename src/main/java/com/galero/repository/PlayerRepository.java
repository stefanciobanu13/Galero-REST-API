package com.galero.repository;

import com.galero.dto.PlayerEditionWinsDTO;
import com.galero.dto.PlayerAllTimeScoresDTO;
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
            "COUNT(DISTINCT m.edition.editionId), " +
            "COUNT(DISTINCT e.editionId)) " +
            "FROM Player p " +
            "JOIN TeamPlayer tp ON tp.player.playerId = p.playerId " +
            "JOIN Team t ON tp.team.teamId = t.teamId " +
            "JOIN Match m ON (m.team1.teamId = t.teamId OR m.team2.teamId = t.teamId) " +
            "JOIN Edition e ON t.edition.editionId = e.editionId " +
            "WHERE m.matchType = 'big_final' " +
            "AND ((m.team1.teamId = t.teamId AND m.team1Score > m.team2Score) " +
            "OR (m.team2.teamId = t.teamId AND m.team2Score > m.team1Score)) " +
            "GROUP BY p.playerId, p.firstName, p.lastName, p.grade " +
            "ORDER BY COUNT(DISTINCT m.edition.editionId) DESC")
    List<PlayerEditionWinsDTO> findTopPlayersByEditionWins();

    @Query("SELECT NEW com.galero.dto.PlayerAllTimeScoresDTO(" +
            "p.playerId, p.firstName, p.lastName, p.grade, COUNT(g.goalId)) " +
            "FROM Player p " +
            "LEFT JOIN Goal g ON g.player.playerId = p.playerId " +
            "GROUP BY p.playerId, p.firstName, p.lastName, p.grade " +
            "HAVING COUNT(g.goalId) > 0 " +
            "ORDER BY COUNT(g.goalId) DESC")
    List<PlayerAllTimeScoresDTO> findTopPlayersByAllTimeScores();
}

