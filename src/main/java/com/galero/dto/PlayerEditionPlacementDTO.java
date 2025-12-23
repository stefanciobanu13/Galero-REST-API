package com.galero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEditionPlacementDTO {
    private Integer editionId;
    private Integer editionNumber;
    private LocalDate date;
    private Integer placement;
    private String finalType; // "big_final" or "small_final"
    private String opponentColor;
    private Integer playerTeamScore;
    private Integer opponentScore;
}
