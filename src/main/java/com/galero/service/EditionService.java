package com.galero.service;

import com.galero.dto.EditionDTO;
import com.galero.dto.EditionFullDetailsDTO;
import com.galero.dto.PlayerDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.*;
import com.galero.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EditionService {

    @Autowired
    private EditionRepository editionRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamPlayerRepository teamPlayerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private GoalRepository goalRepository;

    public EditionDTO createEdition(EditionDTO editionDTO) {
        Edition edition = new Edition();
        edition.setEditionNumber(editionDTO.getEditionNumber());
        edition.setDate(editionDTO.getDate());
        Edition saved = editionRepository.save(edition);
        return convertToDTO(saved);
    }

    public EditionDTO getEditionById(Integer id) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + id));
        return convertToDTO(edition);
    }

    public EditionDTO getEditionByNumber(Integer editionNumber) {
        Edition edition = editionRepository.findByEditionNumber(editionNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with number: " + editionNumber));
        return convertToDTO(edition);
    }

    public List<EditionDTO> getAllEditions() {
        return editionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EditionDTO updateEdition(Integer id, EditionDTO editionDTO) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + id));
        
        edition.setEditionNumber(editionDTO.getEditionNumber());
        edition.setDate(editionDTO.getDate());
        
        Edition updated = editionRepository.save(edition);
        return convertToDTO(updated);
    }

    public void deleteEdition(Integer id) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + id));
        editionRepository.delete(edition);
    }

    @Transactional(readOnly = true)
    public EditionFullDetailsDTO getEditionFullDetails(Integer editionId) {
        Edition edition = editionRepository.findById(editionId)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + editionId));

        // Get all teams for this edition with their players
        List<Team> teams = teamRepository.findByEditionEditionId(editionId);
        List<EditionFullDetailsDTO.TeamWithPlayersDTO> teamDTOs = teams.stream()
                .map(team -> {
                    List<TeamPlayer> teamPlayers = teamPlayerRepository.findByTeamTeamId(team.getTeamId());
                    List<PlayerDTO> playerDTOs = teamPlayers.stream()
                            .map(tp -> new PlayerDTO(
                                    tp.getPlayer().getPlayerId(),
                                    tp.getPlayer().getFirstName(),
                                    tp.getPlayer().getLastName(),
                                    tp.getPlayer().getGrade()))
                            .collect(Collectors.toList());
                    return new EditionFullDetailsDTO.TeamWithPlayersDTO(
                            team.getTeamId(),
                            team.getTeamColor(),
                            playerDTOs);
                })
                .collect(Collectors.toList());

        // Get all matches for this edition with their goals
        List<Match> matches = matchRepository.findByEditionEditionId(editionId);
        List<EditionFullDetailsDTO.MatchWithGoalsDTO> matchDTOs = matches.stream()
                .map(match -> {
                    List<Goal> goals = goalRepository.findByMatchMatchId(match.getMatchId());
                    List<EditionFullDetailsDTO.GoalDetailDTO> goalDTOs = goals.stream()
                            .map(goal -> new EditionFullDetailsDTO.GoalDetailDTO(
                                    goal.getGoalId(),
                                    goal.getGoalType().name(),
                                    goal.getTeam().getTeamId(),
                                    goal.getTeam().getTeamColor(),
                                    goal.getPlayer().getPlayerId(),
                                    goal.getPlayer().getFirstName(),
                                    goal.getPlayer().getLastName()))
                            .collect(Collectors.toList());
                    return new EditionFullDetailsDTO.MatchWithGoalsDTO(
                            match.getMatchId(),
                            match.getMatchType().name(),
                            match.getStage(),
                            match.getTeam1().getTeamId(),
                            match.getTeam1().getTeamColor(),
                            match.getTeam2().getTeamId(),
                            match.getTeam2().getTeamColor(),
                            match.getTeam1Score(),
                            match.getTeam2Score(),
                            goalDTOs);
                })
                .collect(Collectors.toList());

        return new EditionFullDetailsDTO(
                edition.getEditionId(),
                edition.getEditionNumber(),
                edition.getDate(),
                teamDTOs,
                matchDTOs);
    }

    private EditionDTO convertToDTO(Edition edition) {
        return new EditionDTO(edition.getEditionId(), edition.getEditionNumber(), edition.getDate());
    }
}
