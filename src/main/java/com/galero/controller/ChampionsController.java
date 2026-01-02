package com.galero.controller;

import com.galero.dto.PlayerEditionWinsDTO;
import com.galero.dto.PlayerAllTimeScoresDTO;
import com.galero.dto.PlayerPlacementStatsDTO;
import com.galero.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/champions")
@Tag(name = "Champions", description = "API for champions statistics and rankings")
public class ChampionsController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/edition-winners")
    @Operation(summary = "Get top players with the most edition wins", 
               description = "Returns players ranked by the number of editions their team won (big final)")
    public ResponseEntity<List<PlayerEditionWinsDTO>> getTopEditionWinners(
            @Parameter(description = "Number of top results to return", required = true)
            @RequestParam Integer limit) {
        List<PlayerEditionWinsDTO> topPlayers = playerService.getTopPlayersByEditionWins(limit);
        return ResponseEntity.ok(topPlayers);
    }

    @GetMapping("/all-time-scorers")
    @Operation(summary = "Get top all-time scorers", 
               description = "Returns players ranked by total number of goals scored")
    public ResponseEntity<List<PlayerAllTimeScoresDTO>> getTopAllTimeScorers(
            @Parameter(description = "Number of top results to return", required = true)
            @RequestParam Integer limit) {
        List<PlayerAllTimeScoresDTO> topScorers = playerService.getTopPlayersByAllTimeScores(limit);
        return ResponseEntity.ok(topScorers);
    }

    @GetMapping("/placement-stats")
    @Operation(summary = "Get players placement statistics", 
               description = "Returns players with their placement counts: 1st (won big final), 2nd (lost big final), 3rd (won small final), 4th (lost small final)")
    public ResponseEntity<List<PlayerPlacementStatsDTO>> getPlayersPlacementStats(
            @Parameter(description = "Number of top results to return", required = true)
            @RequestParam Integer limit) {
        List<PlayerPlacementStatsDTO> stats = playerService.getPlayersPlacementStats(limit);
        return ResponseEntity.ok(stats);
    }
}
