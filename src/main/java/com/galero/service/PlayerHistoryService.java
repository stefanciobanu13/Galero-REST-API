package com.galero.service;

import com.galero.dto.PlayerEditionPlacementDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Edition;
import com.galero.model.Match;
import com.galero.model.Team;
import com.galero.model.TeamPlayer;
import com.galero.repository.MatchRepository;
import com.galero.repository.PlayerRepository;
import com.galero.repository.TeamPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlayerHistoryService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamPlayerRepository teamPlayerRepository;

    @Autowired
    private MatchRepository matchRepository;

    public List<PlayerEditionPlacementDTO> getPlayerEditionHistory(Integer playerId, Integer limit) {
        // Verify player exists
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player not found with ID: " + playerId);
        }

        // Get all editions where the player participated, ordered by edition number descending
        List<Edition> editions = playerRepository.findEditionsWherePlayerParticipated(playerId);

        // Limit the results
        List<Edition> limitedEditions = editions.subList(0, Math.min(limit, editions.size()));

        List<PlayerEditionPlacementDTO> placements = new ArrayList<>();

        for (Edition edition : limitedEditions) {
            // Find which team the player was in for this edition
            List<TeamPlayer> teamPlayers = teamPlayerRepository.findByPlayerPlayerId(playerId);
            Team playerTeam = null;

            for (TeamPlayer tp : teamPlayers) {
                if (tp.getTeam().getEdition().getEditionId().equals(edition.getEditionId())) {
                    playerTeam = tp.getTeam();
                    break;
                }
            }

            if (playerTeam != null) {
                // Find the final match this team played in
                Match finalMatch = matchRepository.findFinalMatchByEditionAndTeam(edition.getEditionId(), playerTeam.getTeamId())
                        .orElse(null);

                if (finalMatch != null) {
                    PlayerEditionPlacementDTO placement = calculatePlacement(finalMatch, playerTeam);
                    placements.add(placement);
                }
            }
        }

        return placements;
    }

    private PlayerEditionPlacementDTO calculatePlacement(Match finalMatch, Team playerTeam) {
        PlayerEditionPlacementDTO dto = new PlayerEditionPlacementDTO();
        dto.setEditionId(finalMatch.getEdition().getEditionId());
        dto.setEditionNumber(finalMatch.getEdition().getEditionNumber());
        dto.setDate(finalMatch.getEdition().getDate());
        dto.setFinalType(finalMatch.getMatchType().name());

        boolean playerTeamIsTeam1 = finalMatch.getTeam1().getTeamId().equals(playerTeam.getTeamId());
        int playerTeamScore = playerTeamIsTeam1 ? finalMatch.getTeam1Score() : finalMatch.getTeam2Score();
        int opponentScore = playerTeamIsTeam1 ? finalMatch.getTeam2Score() : finalMatch.getTeam1Score();
        Team opponentTeam = playerTeamIsTeam1 ? finalMatch.getTeam2() : finalMatch.getTeam1();

        dto.setPlayerTeamScore(playerTeamScore);
        dto.setOpponentScore(opponentScore);
        dto.setOpponentColor(opponentTeam.getTeamColor());

        // Determine placement based on final type and match result
        boolean playerTeamWon = playerTeamScore > opponentScore;

        if ("big_final".equals(finalMatch.getMatchType().name())) {
            dto.setPlacement(playerTeamWon ? 1 : 2);
        } else { // small_final
            dto.setPlacement(playerTeamWon ? 3 : 4);
        }

        return dto;
    }
}
