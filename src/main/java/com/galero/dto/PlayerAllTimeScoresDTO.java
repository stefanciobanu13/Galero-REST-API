package com.galero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAllTimeScoresDTO {
    private Integer playerId;
    private String firstName;
    private String lastName;
    private Double grade;
    private Long totalGoals;
}
