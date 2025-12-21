package com.galero.service;

import com.galero.dto.GoalDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Goal;
import com.galero.model.Match;
import com.galero.model.Player;
import com.galero.model.Team;
import com.galero.repository.GoalRepository;
import com.galero.repository.MatchRepository;
import com.galero.repository.PlayerRepository;
import com.galero.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public GoalDTO recordGoal(GoalDTO goalDTO) {
        Match match = matchRepository.findById(goalDTO.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with ID: " + goalDTO.getMatchId()));
        
        Team team = teamRepository.findById(goalDTO.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + goalDTO.getTeamId()));
        
        Player player = playerRepository.findById(goalDTO.getPlayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + goalDTO.getPlayerId()));
        
        Goal goal = new Goal();
        goal.setGoalType(goalDTO.getGoalType());
        goal.setMatch(match);
        goal.setTeam(team);
        goal.setPlayer(player);
        
        Goal saved = goalRepository.save(goal);
        return convertToDTO(saved);
    }

    public GoalDTO getGoalById(Integer id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with ID: " + id));
        return convertToDTO(goal);
    }

    public List<GoalDTO> getGoalsByMatch(Integer matchId) {
        return goalRepository.findByMatchMatchId(matchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GoalDTO> getGoalsByTeam(Integer teamId) {
        return goalRepository.findByTeamTeamId(teamId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GoalDTO> getGoalsByPlayer(Integer playerId) {
        return goalRepository.findByPlayerPlayerId(playerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GoalDTO> getGoalsByType(String goalType) {
        return goalRepository.findByGoalType(goalType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GoalDTO> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GoalDTO updateGoal(Integer id, GoalDTO goalDTO) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with ID: " + id));
        
        goal.setGoalType(goalDTO.getGoalType());
        
        Goal updated = goalRepository.save(goal);
        return convertToDTO(updated);
    }

    public void deleteGoal(Integer id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with ID: " + id));
        goalRepository.delete(goal);
    }

    private GoalDTO convertToDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();
        dto.setGoalId(goal.getGoalId());
        dto.setGoalType(goal.getGoalType());
        dto.setMatchId(goal.getMatch().getMatchId());
        dto.setTeamId(goal.getTeam().getTeamId());
        dto.setPlayerId(goal.getPlayer().getPlayerId());
        return dto;
    }
}
