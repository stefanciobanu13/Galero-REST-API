package com.galero.controller;

import com.galero.dto.GoalDTO;
import com.galero.dto.PlayerGoalCountDTO;
import com.galero.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
@Tag(name = "Goals", description = "API for managing goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    @Operation(summary = "Record a new goal")
    public ResponseEntity<GoalDTO> recordGoal(@Valid @RequestBody GoalDTO goalDTO) {
        GoalDTO created = goalService.recordGoal(goalDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get goal by ID")
    public ResponseEntity<GoalDTO> getGoalById(@PathVariable Integer id) {
        GoalDTO goal = goalService.getGoalById(id);
        return ResponseEntity.ok(goal);
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Get all goals in a match")
    public ResponseEntity<List<GoalDTO>> getGoalsByMatch(@PathVariable Integer matchId) {
        List<GoalDTO> goals = goalService.getGoalsByMatch(matchId);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Get all goals scored by a team")
    public ResponseEntity<List<GoalDTO>> getGoalsByTeam(@PathVariable Integer teamId) {
        List<GoalDTO> goals = goalService.getGoalsByTeam(teamId);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Get all goals scored by a player")
    public ResponseEntity<List<GoalDTO>> getGoalsByPlayer(@PathVariable Integer playerId) {
        List<GoalDTO> goals = goalService.getGoalsByPlayer(playerId);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/player/{playerId}/count")
    @Operation(summary = "Get the total number of goals scored by a player")
    public ResponseEntity<PlayerGoalCountDTO> getPlayerGoalCount(@PathVariable Integer playerId) {
        PlayerGoalCountDTO result = goalService.getPlayerGoalCount(playerId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/type/{goalType}")
    @Operation(summary = "Get all goals of a specific type")
    public ResponseEntity<List<GoalDTO>> getGoalsByType(@PathVariable String goalType) {
        List<GoalDTO> goals = goalService.getGoalsByType(goalType);
        return ResponseEntity.ok(goals);
    }

    @GetMapping
    @Operation(summary = "Get all goals")
    public ResponseEntity<List<GoalDTO>> getAllGoals() {
        List<GoalDTO> goals = goalService.getAllGoals();
        return ResponseEntity.ok(goals);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a goal")
    public ResponseEntity<GoalDTO> updateGoal(@PathVariable Integer id, @Valid @RequestBody GoalDTO goalDTO) {
        GoalDTO updated = goalService.updateGoal(id, goalDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a goal")
    public ResponseEntity<Void> deleteGoal(@PathVariable Integer id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
