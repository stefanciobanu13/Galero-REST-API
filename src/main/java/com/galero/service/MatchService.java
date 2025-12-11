package com.galero.service;

import com.galero.dto.MatchDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Edition;
import com.galero.model.Match;
import com.galero.model.Team;
import com.galero.repository.EditionRepository;
import com.galero.repository.MatchRepository;
import com.galero.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EditionRepository editionRepository;

    @Autowired
    private TeamRepository teamRepository;

    public MatchDTO createMatch(MatchDTO matchDTO) {
        Edition edition = editionRepository.findById(matchDTO.getEditionId())
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + matchDTO.getEditionId()));
        
        Team team1 = teamRepository.findById(matchDTO.getTeam1Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team 1 not found with ID: " + matchDTO.getTeam1Id()));
        
        Team team2 = teamRepository.findById(matchDTO.getTeam2Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team 2 not found with ID: " + matchDTO.getTeam2Id()));
        
        Match match = new Match();
        match.setEdition(edition);
        match.setMatchType(Match.MatchType.valueOf(matchDTO.getMatchType()));
        match.setStage(matchDTO.getStage());
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setTeam1Score(matchDTO.getTeam1Score());
        match.setTeam2Score(matchDTO.getTeam2Score());
        
        Match saved = matchRepository.save(match);
        return convertToDTO(saved);
    }

    public MatchDTO getMatchById(Integer id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with ID: " + id));
        return convertToDTO(match);
    }

    public List<MatchDTO> getMatchesByEdition(Integer editionId) {
        return matchRepository.findByEditionEditionId(editionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MatchDTO> getMatchesByTeam(Integer teamId) {
        List<MatchDTO> matches = matchRepository.findByTeam1TeamId(teamId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        matches.addAll(matchRepository.findByTeam2TeamId(teamId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        
        return matches;
    }

    public List<MatchDTO> getMatchesByType(String matchType) {
        return matchRepository.findByMatchType(matchType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MatchDTO updateMatch(Integer id, MatchDTO matchDTO) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with ID: " + id));
        
        match.setMatchType(Match.MatchType.valueOf(matchDTO.getMatchType()));
        match.setStage(matchDTO.getStage());
        match.setTeam1Score(matchDTO.getTeam1Score());
        match.setTeam2Score(matchDTO.getTeam2Score());
        
        Match updated = matchRepository.save(match);
        return convertToDTO(updated);
    }

    public void deleteMatch(Integer id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with ID: " + id));
        matchRepository.delete(match);
    }

    private MatchDTO convertToDTO(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.setMatchId(match.getMatchId());
        dto.setEditionId(match.getEdition().getEditionId());
        dto.setMatchType(match.getMatchType().name());
        dto.setStage(match.getStage());
        dto.setTeam1Id(match.getTeam1().getTeamId());
        dto.setTeam2Id(match.getTeam2().getTeamId());
        dto.setTeam1Score(match.getTeam1Score());
        dto.setTeam2Score(match.getTeam2Score());
        return dto;
    }
}
