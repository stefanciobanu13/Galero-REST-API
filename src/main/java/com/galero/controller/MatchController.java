package com.galero.controller;

import com.galero.dto.MatchDTO;
import com.galero.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@Tag(name = "Matches", description = "API for managing matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping
    @Operation(summary = "Create a new match")
    public ResponseEntity<MatchDTO> createMatch(@Valid @RequestBody MatchDTO matchDTO) {
        MatchDTO created = matchService.createMatch(matchDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get match by ID")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Integer id) {
        MatchDTO match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }

    @GetMapping("/edition/{editionId}")
    @Operation(summary = "Get all matches for a specific edition")
    public ResponseEntity<List<MatchDTO>> getMatchesByEdition(@PathVariable Integer editionId) {
        List<MatchDTO> matches = matchService.getMatchesByEdition(editionId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Get all matches for a specific team")
    public ResponseEntity<List<MatchDTO>> getMatchesByTeam(@PathVariable Integer teamId) {
        List<MatchDTO> matches = matchService.getMatchesByTeam(teamId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/type/{matchType}")
    @Operation(summary = "Get all matches of a specific type")
    public ResponseEntity<List<MatchDTO>> getMatchesByType(@PathVariable String matchType) {
        List<MatchDTO> matches = matchService.getMatchesByType(matchType);
        return ResponseEntity.ok(matches);
    }

    @GetMapping
    @Operation(summary = "Get all matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        List<MatchDTO> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a match")
    public ResponseEntity<MatchDTO> updateMatch(@PathVariable Integer id, @Valid @RequestBody MatchDTO matchDTO) {
        MatchDTO updated = matchService.updateMatch(id, matchDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a match")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}
