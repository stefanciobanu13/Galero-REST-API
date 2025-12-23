package com.galero.controller;

import com.galero.dto.PlayerDTO;
import com.galero.dto.PlayerEditionPlacementDTO;
import com.galero.service.PlayerService;
import com.galero.service.PlayerHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
@Tag(name = "Players", description = "API for managing players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerHistoryService playerHistoryService;

    @PostMapping
    @Operation(summary = "Create a new player")
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        PlayerDTO created = playerService.createPlayer(playerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Integer id) {
        PlayerDTO player = playerService.getPlayerById(id);
        return ResponseEntity.ok(player);
    }

    @GetMapping("/name")
    @Operation(summary = "Get player by first and last name")
    public ResponseEntity<PlayerDTO> getPlayerByName(@RequestParam String firstName, @RequestParam String lastName) {
        PlayerDTO player = playerService.getPlayerByName(firstName, lastName);
        return ResponseEntity.ok(player);
    }

    @GetMapping
    @Operation(summary = "Get all players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a player")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Integer id, @Valid @RequestBody PlayerDTO playerDTO) {
        PlayerDTO updated = playerService.updatePlayer(id, playerDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{playerId}/history")
    @Operation(summary = "Get player's placement history in editions")
    public ResponseEntity<List<PlayerEditionPlacementDTO>> getPlayerEditionHistory(
            @PathVariable Integer playerId,
            @RequestParam(defaultValue = "5")
            @Parameter(description = "Number of recent editions to retrieve (default: 5)") Integer limit) {
        List<PlayerEditionPlacementDTO> history = playerHistoryService.getPlayerEditionHistory(playerId, limit);
        return ResponseEntity.ok(history);
    }
}

