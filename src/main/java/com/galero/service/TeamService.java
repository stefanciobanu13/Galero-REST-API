package com.galero.service;

import com.galero.dto.TeamDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Edition;
import com.galero.model.Team;
import com.galero.repository.EditionRepository;
import com.galero.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EditionRepository editionRepository;

    public TeamDTO createTeam(TeamDTO teamDTO) {
        Edition edition = editionRepository.findById(teamDTO.getEditionId())
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + teamDTO.getEditionId()));
        
        Team team = new Team();
        team.setEdition(edition);
        team.setTeamColor(teamDTO.getTeamColor());
        Team saved = teamRepository.save(team);
        return convertToDTO(saved);
    }

    public TeamDTO getTeamById(Integer id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + id));
        return convertToDTO(team);
    }

    public List<TeamDTO> getTeamsByEdition(Integer editionId) {
        return teamRepository.findByEditionEditionId(editionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TeamDTO updateTeam(Integer id, TeamDTO teamDTO) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + id));
        
        team.setTeamColor(teamDTO.getTeamColor());
        
        Team updated = teamRepository.save(team);
        return convertToDTO(updated);
    }

    public void deleteTeam(Integer id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + id));
        teamRepository.delete(team);
    }

    private TeamDTO convertToDTO(Team team) {
        return new TeamDTO(team.getTeamId(), team.getEdition().getEditionId(), team.getTeamColor());
    }
}
