package com.galero.controller;

import com.galero.dto.TeamDTO;
import com.galero.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@Tag(name = "Teams", description = "API for managing teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    @Operation(summary = "Create a new team")
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO) {
        TeamDTO created = teamService.createTeam(teamDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id) {
        TeamDTO team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    @GetMapping("/edition/{editionId}")
    @Operation(summary = "Get all teams for a specific edition")
    public ResponseEntity<List<TeamDTO>> getTeamsByEdition(@PathVariable Integer editionId) {
        List<TeamDTO> teams = teamService.getTeamsByEdition(editionId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping
    @Operation(summary = "Get all teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a team")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Integer id, @Valid @RequestBody TeamDTO teamDTO) {
        TeamDTO updated = teamService.updateTeam(id, teamDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a team")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
