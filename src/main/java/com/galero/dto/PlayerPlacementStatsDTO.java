package com.galero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPlacementStatsDTO {
    private Integer playerId;
    private String firstName;
    private String lastName;
    private Double grade;
    private Long firstPlaceCount;   // Won big final
    private Long secondPlaceCount;  // Lost big final
    private Long thirdPlaceCount;   // Won small final
    private Long fourthPlaceCount;  // Lost small final
    private Long editionsPlayedCount;
}
