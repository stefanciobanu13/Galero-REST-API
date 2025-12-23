package com.galero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGoalCountDTO {
    private Integer playerId;
    private String firstName;
    private String lastName;
    private Long goalCount;
}
