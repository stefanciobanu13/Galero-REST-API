package com.galero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditionFullDetailsDTO {
    private Integer editionId;
    private Integer editionNumber;
    private LocalDate date;
    private List<TeamWithPlayersDTO> teams;
    private List<MatchWithGoalsDTO> matches;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamWithPlayersDTO {
        private Integer teamId;
        private String teamColor;
        private List<PlayerDTO> players;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchWithGoalsDTO {
        private Integer matchId;
        private String matchType;
        private String stage;
        private Integer team1Id;
        private String team1Color;
        private Integer team2Id;
        private String team2Color;
        private Integer team1Score;
        private Integer team2Score;
        private List<GoalDetailDTO> goals;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoalDetailDTO {
        private Integer goalId;
        private String goalType;
        private Integer teamId;
        private String teamColor;
        private Integer playerId;
        private String playerFirstName;
        private String playerLastName;
    }
}
