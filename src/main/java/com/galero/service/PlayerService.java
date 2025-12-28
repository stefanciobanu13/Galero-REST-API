package com.galero.service;

import com.galero.dto.PlayerDTO;
import com.galero.dto.PlayerEditionWinsDTO;
import com.galero.dto.PlayerAllTimeScoresDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Player;
import com.galero.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setFirstName(playerDTO.getFirstName());
        player.setLastName(playerDTO.getLastName());
        player.setGrade(playerDTO.getGrade());
        Player saved = playerRepository.save(player);
        return convertToDTO(saved);
    }

    public PlayerDTO getPlayerById(Integer id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + id));
        return convertToDTO(player);
    }

    public PlayerDTO getPlayerByName(String firstName, String lastName) {
        Player player = playerRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with name: " + firstName + " " + lastName));
        return convertToDTO(player);
    }

    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO updatePlayer(Integer id, PlayerDTO playerDTO) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + id));
        
        player.setFirstName(playerDTO.getFirstName());
        player.setLastName(playerDTO.getLastName());
        player.setGrade(playerDTO.getGrade());
        
        Player updated = playerRepository.save(player);
        return convertToDTO(updated);
    }

    public void deletePlayer(Integer id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + id));
        playerRepository.delete(player);
    }

    public List<PlayerEditionWinsDTO> getTopPlayersByEditionWins(Integer limit) {
        return playerRepository.findTopPlayersByEditionWins().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<PlayerAllTimeScoresDTO> getTopPlayersByAllTimeScores(Integer limit) {
        return playerRepository.findTopPlayersByAllTimeScores().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private PlayerDTO convertToDTO(Player player) {
        return new PlayerDTO(player.getPlayerId(), player.getFirstName(), player.getLastName(), player.getGrade());
    }
}

