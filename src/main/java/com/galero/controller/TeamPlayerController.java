package com.galero.controller;

import com.galero.dto.TeamPlayerDTO;
import com.galero.service.TeamPlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team-players")
@Tag(name = "Team Players", description = "API for managing player-team associations")
public class TeamPlayerController {

    @Autowired
    private TeamPlayerService teamPlayerService;

    @PostMapping
    @Operation(summary = "Add a player to a team")
    public ResponseEntity<TeamPlayerDTO> addPlayerToTeam(@Valid @RequestBody TeamPlayerDTO teamPlayerDTO) {
        TeamPlayerDTO created = teamPlayerService.addPlayerToTeam(teamPlayerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Get all players in a team")
    public ResponseEntity<List<TeamPlayerDTO>> getPlayersByTeam(@PathVariable Integer teamId) {
        List<TeamPlayerDTO> players = teamPlayerService.getPlayersByTeam(teamId);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Get all teams for a player")
    public ResponseEntity<List<TeamPlayerDTO>> getTeamsByPlayer(@PathVariable Integer playerId) {
        List<TeamPlayerDTO> teams = teamPlayerService.getTeamsByPlayer(playerId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping
    @Operation(summary = "Get all team-player associations")
    public ResponseEntity<List<TeamPlayerDTO>> getAllTeamPlayers() {
        List<TeamPlayerDTO> teamPlayers = teamPlayerService.getAllTeamPlayers();
        return ResponseEntity.ok(teamPlayers);
    }

    @DeleteMapping("/{teamId}/{playerId}")
    @Operation(summary = "Remove a player from a team")
    public ResponseEntity<Void> removePlayerFromTeam(@PathVariable Integer teamId, @PathVariable Integer playerId) {
        teamPlayerService.removePlayerFromTeam(teamId, playerId);
        return ResponseEntity.noContent().build();
    }
}
