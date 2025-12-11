package com.galero.repository;

import com.galero.model.TeamPlayer;
import com.galero.model.TeamPlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, TeamPlayerId> {
    List<TeamPlayer> findByTeamTeamId(Integer teamId);
    List<TeamPlayer> findByPlayerPlayerId(Integer playerId);
}
