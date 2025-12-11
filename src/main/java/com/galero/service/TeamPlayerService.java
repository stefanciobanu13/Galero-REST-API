package com.galero.service;

import com.galero.dto.TeamPlayerDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Player;
import com.galero.model.Team;
import com.galero.model.TeamPlayer;
import com.galero.model.TeamPlayerId;
import com.galero.repository.PlayerRepository;
import com.galero.repository.TeamPlayerRepository;
import com.galero.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamPlayerService {

    @Autowired
    private TeamPlayerRepository teamPlayerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public TeamPlayerDTO addPlayerToTeam(TeamPlayerDTO teamPlayerDTO) {
        Team team = teamRepository.findById(teamPlayerDTO.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamPlayerDTO.getTeamId()));
        
        Player player = playerRepository.findById(teamPlayerDTO.getPlayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + teamPlayerDTO.getPlayerId()));
        
        TeamPlayerId id = new TeamPlayerId(team.getTeamId(), player.getPlayerId());
        TeamPlayer teamPlayer = new TeamPlayer(id, team, player);
        TeamPlayer saved = teamPlayerRepository.save(teamPlayer);
        return convertToDTO(saved);
    }

    public List<TeamPlayerDTO> getPlayersByTeam(Integer teamId) {
        return teamPlayerRepository.findByTeamTeamId(teamId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TeamPlayerDTO> getTeamsByPlayer(Integer playerId) {
        return teamPlayerRepository.findByPlayerPlayerId(playerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TeamPlayerDTO> getAllTeamPlayers() {
        return teamPlayerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void removePlayerFromTeam(Integer teamId, Integer playerId) {
        TeamPlayerId id = new TeamPlayerId(teamId, playerId);
        if (!teamPlayerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team-Player association not found");
        }
        teamPlayerRepository.deleteById(id);
    }

    private TeamPlayerDTO convertToDTO(TeamPlayer teamPlayer) {
        return new TeamPlayerDTO(teamPlayer.getTeam().getTeamId(), teamPlayer.getPlayer().getPlayerId());
    }
}
