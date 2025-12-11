package com.galero.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Integer matchId;

    @NotNull(message = "Edition ID cannot be null")
    private Integer editionId;

    private String matchType;

    private String stage;

    @NotNull(message = "Team 1 ID cannot be null")
    private Integer team1Id;

    @NotNull(message = "Team 2 ID cannot be null")
    private Integer team2Id;

    private Integer team1Score;

    private Integer team2Score;
}
