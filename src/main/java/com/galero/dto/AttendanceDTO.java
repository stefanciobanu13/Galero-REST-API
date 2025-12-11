package com.galero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private Integer attendanceId;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Player ID cannot be null")
    private Integer playerId;

    @NotBlank(message = "Player first name cannot be blank")
    private String playerFirstName;

    @NotBlank(message = "Player last name cannot be blank")
    private String playerLastName;

    @NotNull(message = "Edition ID cannot be null")
    private Integer editionId;
}
