package com.galero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Integer teamId;

    @NotNull(message = "Edition ID cannot be null")
    private Integer editionId;

    @NotBlank(message = "Team color cannot be blank")
    private String teamColor;
}
