package com.galero.controller;

import com.galero.dto.PlayerDTO;
import com.galero.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
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
}
