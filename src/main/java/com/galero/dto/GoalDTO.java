package com.galero.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO {
    private Integer goalId;

    private String goalType;

    @NotNull(message = "Match ID cannot be null")
    private Integer matchId;

    @NotNull(message = "Team ID cannot be null")
    private Integer teamId;

    @NotNull(message = "Player ID cannot be null")
    private Integer playerId;
}
