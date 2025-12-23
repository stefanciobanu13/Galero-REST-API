package com.galero.repository;

import com.galero.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    List<Goal> findByMatchMatchId(Integer matchId);
    List<Goal> findByTeamTeamId(Integer teamId);
    List<Goal> findByPlayerPlayerId(Integer playerId);
    List<Goal> findByGoalType(String goalType);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.player.playerId = :playerId")
    Long countGoalsByPlayerId(@Param("playerId") Integer playerId);
}
